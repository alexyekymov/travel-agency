package dev.overlax.agency.repository;

import dev.overlax.agency.model.Tour;
import dev.overlax.agency.model.Voucher;
import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TourRepository extends JpaRepository<Tour, UUID> {

    List<Voucher> findAllByTourType(TourType tourType);

    List<Voucher> findAllByTransferType(TransferType transferType);

    List<Voucher> findAllByPrice(Double price);

    List<Voucher> findAllByHotelType(HotelType hotelType);
}
