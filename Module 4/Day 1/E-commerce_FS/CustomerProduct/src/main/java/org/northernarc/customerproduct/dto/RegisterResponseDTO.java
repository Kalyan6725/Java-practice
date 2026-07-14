package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class RegisterResponseDTO {
    private Long id;
    private String name;
    private String username;
    private String role;
}
