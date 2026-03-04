package com.trade.dto;

import com.trade.model.UserRole;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Getter
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private LocalDateTime createdAt;

    public UserDTO(Long id, String username, String email,
                   String firstName, String lastName, UserRole role,
                   LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
    }

}