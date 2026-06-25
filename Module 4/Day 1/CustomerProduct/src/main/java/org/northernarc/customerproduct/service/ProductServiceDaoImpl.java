package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.ProductRequestDTO;
import org.northernarc.customerproduct.dto.ProductResponseDTO;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.springframework.stereotype.Service;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.northernarc.customerproduct.model.Product;

import java.util.List;

@Service
public class ProductServiceDaoImpl implements ProductServiceDao {
    @Autowired
    ProductRepository productRepository;
     @Override
    public void deleteById(Long id) {
        if (!productRepository.existsById(Math.toIntExact(id))) {
            throw new ProductNotFound("no product found with id " + id);
        }
        productRepository.deleteById(Math.toIntExact(id));
    }
    @Override
    public ProductResponseDTO getById(Long id) {
        Product product =productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ProductNotFound("no product found with id " + id));
        return mapToDTO(product);
    }
    @Override
    public ProductResponseDTO updateProduct(int id,ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFound("no product found with id " + id));
        product.setName(productRequestDTO.getName());
        product.setBrand(productRequestDTO.getBrand());
        product.setCategory(productRequestDTO.getCategory());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        Product updatedProduct = productRepository.save(product);
        return mapToDTO(updatedProduct);
    }   
    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setBrand(productRequestDTO.getBrand());
        product.setCategory(productRequestDTO.getCategory());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ProductResponseDTO mapToDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
        );
    }

    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> getProductsByPriceRange(
            Double minPrice,
            Double maxPrice) {

        return productRepository.findByPriceRange(minPrice, maxPrice)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> sortByPriceAsc() {
        return productRepository.sortByPriceAsc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> sortByPriceDesc() {
        return productRepository.sortByPriceDesc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> sortByName() {
        return productRepository.sortByName()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> sortByCategory() {
        return productRepository.sortByCategory()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}
