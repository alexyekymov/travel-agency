package dev.overlax.agency.repository;

import dev.overlax.agency.model.Tour;
import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface TourRepository extends JpaRepository<Tour, UUID> {

    @Query("""
            select t from Tour t
            where (:tourType is null or t.tourType = :tourType)
              and (:transferType is null or t.transferType = :transferType)
              and (:hotelType is null or t.hotelType = :hotelType)
              and (:minPrice is null or t.price >= :minPrice)
              and (:maxPrice is null or t.price <= :maxPrice)
            """)
    Page<Tour> filter(@Param("tourType") TourType tourType,
                      @Param("transferType") TransferType transferType,
                      @Param("hotelType") HotelType hotelType,
                      @Param("minPrice") BigDecimal minPrice,
                      @Param("maxPrice") BigDecimal maxPrice,
                      Pageable pageable);
}
