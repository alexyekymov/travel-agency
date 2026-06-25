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
            SELECT t FROM Tour t
            WHERE (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%')))
              AND (:tourType IS NULL OR t.tourType = :tourType)
              AND (:transferType IS NULL OR t.transferType = :transferType)
              AND (:hotelType IS NULL OR t.hotelType = :hotelType)
              AND (:minPrice IS NULL OR t.price >= :minPrice)
              AND (:maxPrice IS NULL OR t.price <= :maxPrice)
              AND (:hot IS NULL OR t.hot = :hot)
            ORDER BY t.hot DESC, t.createdAt DESC
            """)
    Page<Tour> filter(@Param("title") String title,
                      @Param("tourType") TourType tourType,
                      @Param("transferType") TransferType transferType,
                      @Param("hotelType") HotelType hotelType,
                      @Param("minPrice") BigDecimal minPrice,
                      @Param("maxPrice") BigDecimal maxPrice,
                      @Param("hot") Boolean hot,
                      Pageable pageable);
}
