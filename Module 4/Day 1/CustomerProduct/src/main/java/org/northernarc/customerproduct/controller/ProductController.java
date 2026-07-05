package org.northernarc.customerproduct.controller;
import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.ProductRequestDTO;
import org.northernarc.customerproduct.dto.ProductResponseDTO;
import org.northernarc.customerproduct.service.ProductServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductServiceDao productServiceDao;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productServiceDao.addProduct(productRequestDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productServiceDao.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productServiceDao.getAllProducts());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productServiceDao.updateProduct(Math.toIntExact(id),productRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        productServiceDao.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> getByCategory(
            @PathVariable String category) {

        return productServiceDao.getProductsByCategory(category);
    }

    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> getByBrand(
            @PathVariable String brand) {

        return productServiceDao.getProductsByBrand(brand);
    }

    @GetMapping("/price-range")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> getByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {

        return productServiceDao.getProductsByPriceRange(
                minPrice,
                maxPrice
        );
    }

    @GetMapping("/sort/price-asc")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> sortByPriceAsc() {
        return productServiceDao.sortByPriceAsc();
    }

    @GetMapping("/sort/price-desc")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> sortByPriceDesc() {
        return productServiceDao.sortByPriceDesc();
    }

    @GetMapping("/sort/name")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> sortByName() {
        return productServiceDao.sortByName();
    }

    @GetMapping("/sort/category")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> sortByCategory() {
        return productServiceDao.sortByCategory();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponseDTO> searchProducts(
            @RequestParam String keyword) {

        return productServiceDao.searchProducts(keyword);
    }
}
