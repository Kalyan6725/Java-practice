import java.util.List;

public interface ProductDao {
    void addProduct(Product product);
    void updateProduct(String id, Product product);
    void deleteProduct(String id);
    Product getProductById(String id);
    List<Product> getAllProducts();
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByName(String name);
}