package dev.overlax.agency.restcontroller;

import dev.overlax.agency.dto.VoucherDTO;
import dev.overlax.agency.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vouchers")
public class VoucherRestController {
//
//    private final VoucherService service;
//
//    private record ResponseDto(String statusCode, String statusMessage) {
//    }
//
//    private record ListResponseDto(List<VoucherDTO> results) {
//    }
//
//    @PatchMapping("/{voucherId}/status")
//    public ResponseEntity<ResponseDto> changeVoucherStatus(@PathVariable String voucherId, @RequestBody VoucherDTO dto) {
//        VoucherDTO changed = service.changeHotStatus(voucherId, dto);
//        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.getReasonPhrase(),
//                "Voucher status is successfully changed");
//        return ResponseEntity.ok(responseDto);
//    }
//
//    @DeleteMapping("/{voucherId}")
//    public ResponseEntity<ResponseDto> deleteVoucherById(@PathVariable String voucherId) {
//        service.delete(voucherId);
//        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.getReasonPhrase(),
//                String.format("Voucher with Id %s has been deleted", voucherId));
//        return ResponseEntity.ok(responseDto);
//    }
//
//    @PatchMapping("/{voucherId}")
//    public ResponseEntity<ResponseDto> updateVoucher(@PathVariable String voucherId) {
//        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.getReasonPhrase(),
//                "Voucher is successfully updated");
//        return ResponseEntity.ok(responseDto);
//    }
//
//    @PostMapping
//    public ResponseEntity<ResponseDto> createVoucher(@RequestBody VoucherDTO voucherDTO) {
//        VoucherDTO created = service.create(voucherDTO);
//        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.getReasonPhrase(),
//                "Voucher is successfully created");
//        return ResponseEntity.created(URI.create("/api/vouchers/" + created.getId())).contentType(MediaType.APPLICATION_JSON).body(responseDto);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<ListResponseDto> findAllByUserId(@PathVariable String userId) {
//        List<VoucherDTO> vouchersDtoList = service.findAllByUserId(userId);
//        ListResponseDto listResponseDto = new ListResponseDto(vouchersDtoList);
//        return ResponseEntity.ok(listResponseDto);
//    }
//
//    @GetMapping
//    public ResponseEntity<ListResponseDto> findAll() {
//        List<VoucherDTO> vouchersDtoList = service.findAll();
//        ListResponseDto listResponseDto = new ListResponseDto(vouchersDtoList);
//        return ResponseEntity.ok(listResponseDto);
//    }
}

