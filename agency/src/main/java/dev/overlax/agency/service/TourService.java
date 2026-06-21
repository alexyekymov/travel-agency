package dev.overlax.agency.service;

import dev.overlax.agency.dto.TourFilterRequest;
import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.mapper.TourMapper;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final ImageStorageService imageStorageService;

    public Page<TourResponse> findAll(TourFilterRequest filter, Pageable pageable) {
        Page<Tour> tours = tourRepository.filter(
                filter.tourType(),
                filter.transferType(),
                filter.hotelType(),
                filter.minPrice(),
                filter.maxPrice(),
                pageable);
        return tours.map(tourMapper::toDto);
    }

    public TourResponse findById(UUID id) {
        log.debug("Finding tour by id: {}", id);
        TourResponse tour = tourRepository.findById(id)
                .map(tourMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + id));
        log.info("Tour found: {}", id);
        return tour;
    }

    public TourResponse create(TourRequest request) {
        Tour tour = tourMapper.toEntity(request);
        tour.setImageName(imageStorageService.save(request.image()));
        Tour saved = tourRepository.save(tour);
        log.info("Tour created: {}", saved.getId());
        return tourMapper.toDto(saved);
    }
}
