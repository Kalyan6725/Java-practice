package org.northernarc.customerproduct.service;
import org.northernarc.customerproduct.dto.ProductRequestDTO;
import org.northernarc.customerproduct.dto.ProductResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ProductServiceDao {
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id);
    public ProductResponseDTO getById(Long id);
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDTO updateProduct(int id,ProductRequestDTO productRequestDTO);
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO);
    public List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getProductsByCategory(String category);
    List<ProductResponseDTO> getProductsByBrand(String brand);
    List<ProductResponseDTO> getProductsByPriceRange(Double minPrice, Double maxPrice);
    List<ProductResponseDTO> sortByPriceAsc();
    List<ProductResponseDTO> sortByPriceDesc();
    List<ProductResponseDTO> sortByName();
    List<ProductResponseDTO> sortByCategory();
    List<ProductResponseDTO> searchProducts(String keyword);
}
