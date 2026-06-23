package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TourResponseMapper extends MappableToDto<Tour, TourResponse> {

    @Mapping(target = "imageName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Tour toEntity(TourRequest request);

    @Mapping(target = "image", ignore = true)
    TourRequest toRequest(Tour tour);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntity(TourRequest request, @MappingTarget Tour tour);
}
