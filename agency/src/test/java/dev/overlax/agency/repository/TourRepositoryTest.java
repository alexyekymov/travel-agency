package dev.overlax.agency.repository;

import dev.overlax.agency.model.Tour;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/tours.sql")
class TourRepositoryTest {

    private static final PageRequest PAGE = PageRequest.of(0, 10);

    @Autowired
    private TourRepository tourRepository;

    @Test
    void givenNoCriteria_whenFilter_thenReturnsAllOrderedByHotThenCreatedDesc() {
        Page<Tour> result = tourRepository.filter(null, null, null, null, null, null, null, PAGE);

        assertThat(result.getTotalElements()).isEqualTo(4);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactly("Sahara Expedition", "Aegean Cruise", "Tuscan Vineyards", "Paris City Break");
    }

    @Test
    void givenTourType_whenFilter_thenReturnsMatchingTours() {
        Page<Tour> result = tourRepository.filter(null, TourType.LEISURE, null, null, null, null, null, PAGE);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactlyInAnyOrder("Paris City Break", "Tuscan Vineyards");
    }

    @Test
    void givenTransferType_whenFilter_thenReturnsMatchingTours() {
        Page<Tour> result = tourRepository.filter(null, null, TransferType.BUS, null, null, null, null, PAGE);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactly("Aegean Cruise");
    }

    @Test
    void givenPriceRange_whenFilter_thenReturnsToursInRange() {
        Page<Tour> result = tourRepository.filter(null, null, null, null,
                new BigDecimal("1000"), new BigDecimal("1500"), null, PAGE);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactlyInAnyOrder("Paris City Break", "Tuscan Vineyards");
    }

    @Test
    void givenHotFlag_whenFilter_thenReturnsHotToursOrdered() {
        Page<Tour> result = tourRepository.filter(null, null, null, null, null, null, true, PAGE);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactly("Sahara Expedition", "Aegean Cruise");
    }

    @Test
    void givenMultipleCriteria_whenFilter_thenReturnsToursMatchingAll() {
        Page<Tour> result = tourRepository.filter(null, TourType.LEISURE, null, null,
                null, new BigDecimal("1200"), null, PAGE);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactly("Paris City Break");
    }

    @Test
    void givenSearchTitle_whenFilter_thenReturnsMatchingToursCaseInsensitive() {
        Page<Tour> result = tourRepository.filter("par", null, null, null, null, null, null, PAGE);

        assertThat(result.getContent())
                .extracting(Tour::getTitle)
                .containsExactly("Paris City Break");
    }
}
