package dev.overlax.agency.controller;

import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class TourControllerTest {

    @Mock
    private TourService tourService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TourController(tourService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givenRequest_whenGetAll_thenRendersIndexWithTours() throws Exception {
        when(tourService.findAll(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/tours"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tours"));
    }

    @Test
    void givenTourId_whenGetOne_thenRendersDetail() throws Exception {
        UUID id = UUID.randomUUID();
        when(tourService.findById(id)).thenReturn(org.mockito.Mockito.mock(TourResponse.class));

        mockMvc.perform(get("/tours/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("tours/detail"))
                .andExpect(model().attributeExists("tour"));
    }

    @Test
    void givenRequest_whenNewForm_thenRendersForm() throws Exception {
        mockMvc.perform(get("/tours/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tours/form"));
    }

    @Test
    void givenValidTour_whenCreate_thenRedirectsToCreatedTour() throws Exception {
        UUID id = UUID.randomUUID();
        TourResponse created = org.mockito.Mockito.mock(TourResponse.class);
        when(created.id()).thenReturn(id);
        when(tourService.create(any(TourRequest.class))).thenReturn(created);

        mockMvc.perform(post("/tours")
                        .param("title", "Carpathian Retreat")
                        .param("price", "1200.00")
                        .param("arrivalDate", "2026-07-01")
                        .param("evictionDate", "2026-07-10")
                        .param("tourType", "LEISURE")
                        .param("hotelType", "FOUR_STARS")
                        .param("transferType", "BUS"))
                .andExpect(redirectedUrl("/tours/" + id));

        verify(tourService).create(any(TourRequest.class));
    }

    @Test
    void givenInvalidTour_whenCreate_thenReturnsFormAndDoesNotCreate() throws Exception {
        mockMvc.perform(post("/tours")
                        .param("price", "1200.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("tours/form"));

        verify(tourService, never()).create(any());
    }

    @Test
    void givenValidTour_whenUpdate_thenRedirectsToDashboard() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/tours/{id}", id)
                        .param("title", "Carpathian Retreat")
                        .param("price", "1200.00")
                        .param("arrivalDate", "2026-07-01")
                        .param("evictionDate", "2026-07-10")
                        .param("tourType", "LEISURE")
                        .param("hotelType", "FOUR_STARS")
                        .param("transferType", "BUS"))
                .andExpect(redirectedUrl("/tours/manage"));

        verify(tourService).update(any(UUID.class), any(TourRequest.class));
    }

    @Test
    void givenTourId_whenToggleHot_thenRedirectsToDashboard() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/tours/{id}/hot", id))
                .andExpect(redirectedUrl("/tours/manage"));

        verify(tourService).toggleHot(id);
    }

    @Test
    void givenTourId_whenDelete_thenRedirectsToDashboard() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/tours/{id}/delete", id))
                .andExpect(redirectedUrl("/tours/manage"));

        verify(tourService).delete(id);
    }
}
