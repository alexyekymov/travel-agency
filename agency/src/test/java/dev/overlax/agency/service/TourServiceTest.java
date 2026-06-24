package dev.overlax.agency.service;

import dev.overlax.agency.dto.TourFilterRequest;
import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.mapper.TourResponseMapper;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import dev.overlax.agency.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TourServiceTest {

    @Mock
    private TourRepository tourRepository;
    @Mock
    private TourResponseMapper tourResponseMapper;
    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private TourService service;

    private TourRequest tourRequest(MultipartFile image) {
        return new TourRequest("Carpathian Retreat", "Mountain cabins and pine air",
                new BigDecimal("1200.00"), LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 10),
                TourType.LEISURE, HotelType.FOUR_STARS, TransferType.BUS, false, image);
    }

    @Test
    void givenExistingTour_whenFindById_thenReturnsMappedTour() {
        UUID id = UUID.randomUUID();
        Tour tour = new Tour();
        TourResponse response = mock(TourResponse.class);
        when(tourRepository.findById(id)).thenReturn(Optional.of(tour));
        when(tourResponseMapper.toDto(tour)).thenReturn(response);

        assertThat(service.findById(id)).isSameAs(response);
    }

    @Test
    void givenMissingTour_whenFindById_thenThrows() {
        UUID id = UUID.randomUUID();
        when(tourRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void givenTourRequestWithImage_whenCreate_thenStoresImageAndPersists() {
        MultipartFile image = mock(MultipartFile.class);
        TourRequest request = tourRequest(image);
        Tour entity = new Tour();
        TourResponse response = mock(TourResponse.class);

        when(tourResponseMapper.toEntity(request)).thenReturn(entity);
        when(imageStorageService.save(image)).thenReturn("carpathian.jpg");
        when(tourRepository.save(entity)).thenReturn(entity);
        when(tourResponseMapper.toDto(entity)).thenReturn(response);

        TourResponse result = service.create(request);

        assertThat(result).isSameAs(response);
        assertThat(entity.getImageName()).isEqualTo("carpathian.jpg");
    }

    @Test
    void givenMissingTour_whenUpdate_thenThrows() {
        UUID id = UUID.randomUUID();
        when(tourRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, tourRequest(null)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void givenNewImage_whenUpdate_thenReplacesImage() {
        UUID id = UUID.randomUUID();
        Tour tour = new Tour();
        tour.setImageName("old.jpg");
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        TourRequest request = tourRequest(image);

        when(tourRepository.findById(id)).thenReturn(Optional.of(tour));
        when(imageStorageService.save(image)).thenReturn("new.jpg");
        when(tourResponseMapper.toDto(tour)).thenReturn(mock(TourResponse.class));

        service.update(id, request);

        verify(tourResponseMapper).updateEntity(request, tour);
        assertThat(tour.getImageName()).isEqualTo("new.jpg");
    }

    @Test
    void givenNoImage_whenUpdate_thenKeepsExistingImage() {
        UUID id = UUID.randomUUID();
        Tour tour = new Tour();
        tour.setImageName("keep.jpg");
        TourRequest request = tourRequest(null);

        when(tourRepository.findById(id)).thenReturn(Optional.of(tour));
        when(tourResponseMapper.toDto(tour)).thenReturn(mock(TourResponse.class));

        service.update(id, request);

        verify(imageStorageService, never()).save(any());
        assertThat(tour.getImageName()).isEqualTo("keep.jpg");
    }

    @Test
    void givenTour_whenToggleHot_thenFlipsFlag() {
        UUID id = UUID.randomUUID();
        Tour tour = new Tour();
        tour.setHot(false);
        when(tourRepository.findById(id)).thenReturn(Optional.of(tour));

        service.toggleHot(id);

        assertThat(tour.getHot()).isTrue();
    }

    @Test
    void givenExistingTour_whenDelete_thenRemovesTour() {
        UUID id = UUID.randomUUID();
        when(tourRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(tourRepository).deleteById(id);
    }

    @Test
    void givenMissingTour_whenDelete_thenThrows() {
        UUID id = UUID.randomUUID();
        when(tourRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
        verify(tourRepository, never()).deleteById(any());
    }

    @Test
    void givenFilter_whenFindAll_thenMapsPage() {
        TourFilterRequest filter = new TourFilterRequest(null, null, null, null, null, null);
        PageRequest pageable = PageRequest.of(0, 10);
        Tour tour = new Tour();
        TourResponse response = mock(TourResponse.class);
        Page<Tour> page = new PageImpl<>(List.of(tour));

        when(tourRepository.filter(null, null, null, null, null, null, pageable)).thenReturn(page);
        when(tourResponseMapper.toDto(tour)).thenReturn(response);

        Page<TourResponse> result = service.findAll(filter, pageable);

        assertThat(result.getContent()).containsExactly(response);
    }
}
