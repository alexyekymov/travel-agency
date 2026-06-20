package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TourMapper extends Mappable<Tour, TourResponse> {

    @Mapping(target = "imageName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Tour toEntity(TourRequest request);
}
