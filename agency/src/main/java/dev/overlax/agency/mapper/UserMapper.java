package dev.overlax.agency.mapper;

import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toUser(UserDTO userDTO);

    UserDTO toUserDTO(User user);
}
