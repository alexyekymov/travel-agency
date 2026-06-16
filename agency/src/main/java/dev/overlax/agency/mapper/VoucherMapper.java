package dev.overlax.agency.mapper;


import dev.overlax.agency.dto.VoucherDTO;
import dev.overlax.agency.model.Voucher;

public interface VoucherMapper {
    Voucher toVoucher(VoucherDTO voucherDTO);

    VoucherDTO toVoucherDTO(Voucher voucher);
}
