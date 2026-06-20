package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserDTO userDTO);

    UserDTO toUserDTO(User user);
}
