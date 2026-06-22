package org.northernarc.customerproduct.service;
import org.northernarc.customerproduct.model.Product;

import java.util.List;

public interface ProductServiceDao {
    public void deleteById(Long id);
    public Product getById(Long id);
    public Product updateProduct(Product product);
    public Product addProduct(Product product);
    public List<Product> getAllProducts();
}
