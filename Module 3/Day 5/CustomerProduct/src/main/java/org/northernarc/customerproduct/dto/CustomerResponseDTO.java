package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.customerproduct.model.Order;

import java.util.List;
@Data
@AllArgsConstructor
public class CustomerResponseDTO {
    private int id;
    private String name;
    private List<OrderResponseDTO> orders;
}
