package com.trade.dto;


import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AuthResponseDTO {

    private String token;
    private String tokenType;
    private UserDTO user;

    public AuthResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.tokenType = "Bearer";
        this.user = user;
    }

}