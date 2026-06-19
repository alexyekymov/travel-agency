package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.TourDto;
import dev.overlax.agency.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TourMapper extends Mappable<Tour, TourDto> {

}
