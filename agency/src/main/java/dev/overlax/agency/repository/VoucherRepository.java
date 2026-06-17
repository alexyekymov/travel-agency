package dev.overlax.agency.repository;

import dev.overlax.agency.model.HotelType;
import dev.overlax.agency.model.TourType;
import dev.overlax.agency.model.TransferType;
import dev.overlax.agency.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    List<Voucher> findAllByUserId(UUID userId);

    List<Voucher> findAllByTourType(TourType tourType);

    List<Voucher> findAllByTransferType(TransferType transferType);

    List<Voucher> findAllByPrice(Double price);

    List<Voucher> findAllByHotelType(HotelType hotelType);
}
