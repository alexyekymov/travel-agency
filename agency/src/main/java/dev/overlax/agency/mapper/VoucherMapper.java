package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.VoucherDTO;
import dev.overlax.agency.model.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoucherMapper {
    Voucher toVoucher(VoucherDTO voucherDTO);

    VoucherDTO toVoucherDTO(Voucher voucher);

    void updateFromDTO(VoucherDTO voucherDTO, @MappingTarget Voucher voucher);
}
