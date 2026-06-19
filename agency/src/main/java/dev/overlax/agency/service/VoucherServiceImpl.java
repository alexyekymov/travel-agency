package dev.overlax.agency.service;

import dev.overlax.agency.dto.VoucherDTO;
import dev.overlax.agency.mapper.VoucherMapper;
import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.Voucher;
import dev.overlax.agency.repository.VoucherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

//    private final VoucherRepository repository;
//    private final VoucherMapper mapper;
//
//    @Override
//    @Transactional
//    public VoucherDTO create(VoucherDTO voucherDTO) {
//        Voucher newVoucher = mapper.toVoucher(voucherDTO);
//        Voucher saved = repository.save(newVoucher);
//        return mapper.toVoucherDTO(saved);
//    }
//
//    @Override
//    public VoucherDTO order(String id, String userId) {
//        return null;
//    }
//
//    @Override
//    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
//        Voucher dummy = mapper.toVoucher(voucherDTO);
//        UUID resourceId = UUID.fromString(id);
//        if (!resourceId.equals(dummy.getId())) {
//            throw new IllegalArgumentException("Id in path and body do not match");
//        }
//
//        return repository.findById(resourceId)
//                .map(found -> {
//                    mapper.updateFromDTO(voucherDTO, found);
//                    Voucher saved = repository.save(found);
//                    return mapper.toVoucherDTO(saved);
//                }).orElseThrow(() -> new EntityNotFoundException(
//                        String.format("Voucher with id: %s not found", resourceId)));
//    }
//
//    @Override
//    public void delete(String voucherId) {
//        UUID id = UUID.fromString(voucherId);
//        repository.deleteById(id);
//    }
//
//    @Override
//    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
//        UUID resourceId = UUID.fromString(id);
//
//        return repository.findById(resourceId)
//                .map(found -> {
//                    found.setHot(voucherDTO.getIsHot());
//                    repository.save(found);
//                    return mapper.toVoucherDTO(found);
//                }).orElseThrow(() -> new EntityNotFoundException(
//                        String.format("Voucher with id: %s not found", resourceId)));
//    }
//
//    @Override
//    public List<VoucherDTO> findAllByUserId(String userId) {
//        UUID id = UUID.fromString(userId);
//        return null;
//    }
//
//    @Override
//    public List<VoucherDTO> findAllByTourType(TourType tourType) {
//        return List.of();
//    }
//
//    @Override
//    public List<VoucherDTO> findAllByTransferType(String transferType) {
//        return List.of();
//    }
//
//    @Override
//    public List<VoucherDTO> findAllByPrice(Double price) {
//        return List.of();
//    }
//
//    @Override
//    public List<VoucherDTO> findAllByHotelType(HotelType hotelType) {
//        return List.of();
//    }
//
//    @Override
//    public List<VoucherDTO> findAll() {
//        return repository.findAll()
//                .stream()
//                .map(mapper::toVoucherDTO)
//                .toList();
//    }
}
