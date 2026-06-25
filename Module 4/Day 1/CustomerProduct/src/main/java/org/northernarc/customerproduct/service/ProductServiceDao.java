package org.northernarc.customerproduct.service;
import org.northernarc.customerproduct.dto.ProductRequestDTO;
import org.northernarc.customerproduct.dto.ProductResponseDTO;

import java.util.List;

public interface ProductServiceDao {
    public void deleteById(Long id);
    public ProductResponseDTO getById(Long id);
    public ProductResponseDTO updateProduct(int id,ProductRequestDTO productRequestDTO);
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
