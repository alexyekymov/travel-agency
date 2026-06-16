package dev.overlax.agency.mapper;


import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.User;

public interface UserMapper {
    User toUser(UserDTO userDTO);

    UserDTO toUserDTO(User user);
}
