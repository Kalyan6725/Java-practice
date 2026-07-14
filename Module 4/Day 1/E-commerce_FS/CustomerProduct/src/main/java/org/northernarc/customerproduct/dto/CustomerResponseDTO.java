package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String username;
    private String role;
    private List<OrderSummaryDTO> orders;
}
