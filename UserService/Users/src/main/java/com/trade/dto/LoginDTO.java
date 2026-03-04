package com.trade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
