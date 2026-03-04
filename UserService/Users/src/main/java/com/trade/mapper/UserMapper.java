package com.trade.mapper;

import com.trade.dto.RegisterDTO;
import com.trade.dto.UserDTO;
import com.trade.model.User;
import com.trade.model.UserRole;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public User toUser(RegisterDTO userDTO) {
        return new User(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getRole()
        );
    }
}
