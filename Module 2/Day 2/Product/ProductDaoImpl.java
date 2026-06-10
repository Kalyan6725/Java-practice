import java.util.ArrayList;
import java.util.List;

class ProductDaoImpl implements ProductDao {
    private List<Product> productList = new ArrayList<>();

    @Override
    public void addProduct(Product product) {
        productList.add(product);
    }

    @Override
    public void updateProduct(String id, Product product) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equals(id)) {
                productList.set(i, product);
                break;
            }
        }
    }

    @Override
    public void deleteProduct(String id) {
        //productList.removeIf(product -> product.getId().equals(id));
        //Without lambda expression
        for (Product p : productList) {
            if (p.getId().equals(id)) {
                productList.remove(p);
                break;
            }
        }
    }

    @Override
    public Product getProductById(String id) {
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    @Override
    public List<Product> findByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        return result;
    }

    @Override
    public List<Product> findByName(String name) {
        List<Product> result= new ArrayList<>();
        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(name)){
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<Product> findByBrand(String brand) {
        List<Product> result= new ArrayList<>();
        for (Product p : productList) {
            if (p.getBrand().equalsIgnoreCase(brand)){
                result.add(p);
            }
        }
        return result;
    }
}