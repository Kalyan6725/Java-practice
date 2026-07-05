package org.northernarc.jwt_demo.dto;

import lombok.Data;

@Data
public class JwtRequestDTO {
    private String username;
    private String password;
}
