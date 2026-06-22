package org.northernarc.customerproduct.service;

import org.springframework.stereotype.Service;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.northernarc.customerproduct.model.Product;

@Service
public class ProductServiceDaoImpl implements ProductServiceDao {
    @Autowired
    ProductRepository productRepository;
     @Override
    public void deleteById(Long id) {
        productRepository.deleteById(Math.toIntExact(id));
    }
    @Override
    public Product getById(Long id) {
        return productRepository.findById(Math.toIntExact(id)).orElse(null);
    }
    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }   
    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}
