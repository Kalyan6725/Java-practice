package org.northernarc.customerproduct.service;

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
    public Product getById(Long id) {
        return productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ProductNotFound("no product found with id " + id));
    }
    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }   
    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
