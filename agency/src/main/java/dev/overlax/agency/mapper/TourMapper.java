package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.TourDto;
import dev.overlax.agency.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TourMapper extends Mappable<Tour, TourDto> {

}
