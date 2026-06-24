package dev.overlax.agency.restcontroller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class VoucherRestControllerTest {
//
//    @MockBean
//    private VoucherService voucherService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    void findAll_Successfully() throws Exception {
//        List<VoucherDTO> voucherDTOList = new ArrayList<>();
//        when(voucherService.findAll()).thenReturn(voucherDTOList);
//        MvcResult result = mockMvc.perform(get("/api/vouchers"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String responseBody = result.getResponse().getContentAsString();
//
//        JsonNode rootNode = objectMapper.readTree(responseBody);
//        JsonNode resultsNode = rootNode.path("results");
//
//        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {
//        });
//        assertEquals(voucherDTOList, responseVoucherDTOList);
//    }
//
//    @Test
//    @WithMockUser
//    void findAllByUserId_Successfully() throws Exception {
//        String userId = String.valueOf(UUID.randomUUID());
//        List<VoucherDTO> voucherDTOList = new ArrayList<>();
//
//        when(voucherService.findAllByUserId(userId)).thenReturn(voucherDTOList);
//
//        MvcResult result = mockMvc.perform(get("/api/vouchers/user/" + userId))
//                .andExpect(status().isOk())
//                .andReturn();
//        String responseBody = result.getResponse().getContentAsString();
//
//        JsonNode rootNode = objectMapper.readTree(responseBody);
//        JsonNode resultsNode = rootNode.path("results");
//
//        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {
//        });
//
//        assertEquals(voucherDTOList, responseVoucherDTOList);
//    }
//
//    @Test
//    @WithMockUser(authorities = "ROLE_ADMIN")
//    void createVoucher_ValidData_SuccessfullyCreated() throws Exception {
//        VoucherDTO voucherDTO = new VoucherDTO();
//        voucherDTO.setTitle("SummerSale2024");
//        voucherDTO.setDescription("Summer Sale Voucher Description");
//        voucherDTO.setPrice(299.99);
//        voucherDTO.setTourType(TourType.ADVENTURE.name());
//        voucherDTO.setTransferType(TransferType.PLANE.name());
//        voucherDTO.setHotelType(HotelType.FIVE_STARS.name());
//        voucherDTO.setStatus(VoucherStatus.PAID.name());
//        voucherDTO.setArrivalDate(LocalDate.of(2024, 6, 15));
//        voucherDTO.setEvictionDate(LocalDate.of(2024, 6, 20));
//        voucherDTO.setUserId(UUID.randomUUID());
//        voucherDTO.setIsHot(false);
//
//        String expectedStatusCode = "OK";
//        String expectedMessage = "Voucher is successfully created";
//
//        when(voucherService.create(any(VoucherDTO.class))).thenReturn(voucherDTO);
//
//        mockMvc.perform(post("/api/vouchers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
//                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
//    }
//
//    @Test
//    @WithMockUser(authorities = "ROLE_ADMIN")
//    void updateVoucher_ValidData_SuccessfullyUpdated() throws Exception {
//        VoucherDTO voucherDTO = new VoucherDTO();
//        voucherDTO.setTitle("UpdatedTitle");
//        voucherDTO.setDescription("Updated description");
//        voucherDTO.setPrice(499.99);
//        voucherDTO.setTourType(TourType.SAFARI.name());
//        voucherDTO.setTransferType(TransferType.JEEPS.name());
//        voucherDTO.setHotelType(HotelType.THREE_STARS.name());
//        voucherDTO.setStatus(VoucherStatus.PAID.name());
//        voucherDTO.setArrivalDate(LocalDate.of(2024, 7, 15));
//        voucherDTO.setEvictionDate(LocalDate.of(2024, 7, 20));
//        voucherDTO.setUserId(UUID.randomUUID());
//        voucherDTO.setIsHot(true);
//
//        String voucherId = String.valueOf(UUID.randomUUID());
//        String expectedStatusCode = "OK";
//        String expectedMessage = "Voucher is successfully updated";
//
//        when(voucherService.update(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);
//
//        mockMvc.perform(patch("/api/vouchers/" + voucherId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
//                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
//    }
//
//    @Test
//    @WithMockUser(authorities = "ROLE_ADMIN")
//    void deleteVoucherById_VoucherExists_SuccessfullyDeleted() throws Exception {
//        String voucherId = String.valueOf(UUID.randomUUID());
//        String expectedStatusCode = "OK";
//        String expectedMessage = String.format("Voucher with Id %s has been deleted", voucherId);
//
//        doNothing().when(voucherService).delete(voucherId);
//
//        mockMvc.perform(delete("/api/vouchers/" + voucherId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
//                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
//
//        verify(voucherService, times(1)).delete(voucherId);
//    }
//
//    @Test
//    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_MANAGER"})
//    void changeVoucherStatus_ValidData_SuccessfullyChanged() throws Exception {
//        VoucherDTO voucherDTO = new VoucherDTO();
//        voucherDTO.setIsHot(true);
//
//        String voucherId = String.valueOf(UUID.randomUUID());
//        String expectedStatusCode = "OK";
//        String expectedMessage = "Voucher status is successfully changed";
//
//        when(voucherService.changeHotStatus(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);
//
//        mockMvc.perform(patch("/api/vouchers/" + voucherId + "/status")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
//                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
//
//        verify(voucherService, times(1)).changeHotStatus(eq(voucherId), any(VoucherDTO.class));
//    }
}
