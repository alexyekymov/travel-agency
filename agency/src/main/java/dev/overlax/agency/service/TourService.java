package dev.overlax.agency.service;

import dev.overlax.agency.dto.TourFilterRequest;
import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.mapper.TourResponseMapper;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourResponseMapper tourResponseMapper;
    private final ImageStorageService imageStorageService;

    public Page<TourResponse> findAll(TourFilterRequest filter, Pageable pageable) {
        Page<Tour> tours = tourRepository.filter(
                filter.tourType(),
                filter.transferType(),
                filter.hotelType(),
                filter.minPrice(),
                filter.maxPrice(),
                filter.hot(),
                pageable);
        return tours.map(tourResponseMapper::toDto);
    }

    public TourResponse findById(UUID id) {
        log.debug("Finding tour by id: {}", id);
        TourResponse tour = tourRepository.findById(id)
                .map(tourResponseMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + id));
        log.info("Tour found: {}", id);
        return tour;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TourResponse create(TourRequest request) {
        Tour tour = tourResponseMapper.toEntity(request);
        tour.setImageName(imageStorageService.save(request.image()));
        Tour saved = tourRepository.save(tour);
        log.info("Tour created: {}", saved.getId());
        return tourResponseMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TourRequest getForEdit(UUID id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + id));
        return tourResponseMapper.toRequest(tour);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TourResponse update(UUID id, TourRequest request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + id));

        tourResponseMapper.updateEntity(request, tour);
        if (request.image() != null && !request.image().isEmpty()) {
            tour.setImageName(imageStorageService.save(request.image()));
        }

        log.info("Tour updated: {}", id);
        return tourResponseMapper.toDto(tour);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Transactional
    public void toggleHot(UUID id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + id));
        tour.setHot(!Boolean.TRUE.equals(tour.getHot()));
        log.info("Tour {} hot set to {}", id, tour.getHot());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        if (!tourRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour not found: " + id);
        }
        tourRepository.deleteById(id);
        log.info("Tour deleted: {}", id);
    }

}
