package dev.overlax.agency.service;

import dev.overlax.agency.dto.TourDto;
import dev.overlax.agency.mapper.TourMapper;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    public List<TourDto> findAll(Pageable pageable) {
        Page<Tour> tours = tourRepository.findAll(pageable);
        return tourMapper.toDtoList(tours.toList());
    }
}
