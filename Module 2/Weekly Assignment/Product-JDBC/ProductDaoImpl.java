import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

class ProductDaoImpl implements ProductDao {
    private static final String TABLE_NAME = "products";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS products (id varchar(20) PRIMARY KEY, name varchar(50), category varchar(50), brand varchar(50), price int, discount int, rating double precision)";

    public ProductDaoImpl() {
        try {
            Connection conn = DBManager.getConnection();
            if (shouldResetTable(conn)) {
                try (Statement statement = conn.createStatement()) {
                    statement.executeUpdate("DROP TABLE IF EXISTS " + TABLE_NAME);
                }
            }
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate(CREATE_TABLE_SQL);
            }
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    private boolean shouldResetTable(Connection conn) throws SQLException {
        Set<String> columns = new HashSet<>();
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, TABLE_NAME, null)) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
        }
        if (columns.isEmpty()) {
            return false;
        }
        return !(columns.contains("id")
                && columns.contains("name")
                && columns.contains("category")
                && columns.contains("brand")
                && columns.contains("price")
                && columns.contains("discount")
                && columns.contains("rating"));
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getString("id"));
        product.setName(rs.getString("name"));
        product.setCategory(rs.getString("category"));
        product.setBrand(rs.getString("brand"));
        product.setPrice(rs.getInt("price"));
        product.setDiscount(rs.getInt("discount"));
        product.setRating(rs.getDouble("rating"));
        return product;
    }

    @Override
    public void addProduct(Product product) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "INSERT INTO products (id, name, category, brand, price, discount, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getCategory());
            pstmt.setString(4, product.getBrand());
            pstmt.setInt(5, product.getPrice());
            pstmt.setInt(6, product.getDiscount());
            pstmt.setDouble(7, product.getRating());
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(String id, Product product) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "UPDATE products SET name = ?, category = ?, brand = ?, price = ?, discount = ?, rating = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setString(3, product.getBrand());
            pstmt.setInt(4, product.getPrice());
            pstmt.setInt(5, product.getDiscount());
            pstmt.setDouble(6, product.getRating());
            pstmt.setString(7, product.getId());
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(String id) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "DELETE FROM products WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Product deleted successfully.");
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public Product getProductById(String id) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Product product = mapProduct(rs);
                DBManager.closeConnection(conn);
                return product;
            }
        } catch (SQLException e) {
            System.out.println("Error finding product: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM products";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapProduct(rs));
            }
            DBManager.closeConnection(conn);
            return productList;
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Product> findByCategory(String category) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM products WHERE category = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            List<Product> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapProduct(rs));
            }
            DBManager.closeConnection(conn);
            return result;
        } catch (SQLException e) {
            System.out.println("Error finding products by category: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Product> findByName(String name) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM products WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            List<Product> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapProduct(rs));
            }
            DBManager.closeConnection(conn);
            return result;
        } catch (SQLException e) {
            System.out.println("Error finding products by name: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Product> findByBrand(String brand) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM products WHERE brand = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, brand);
            ResultSet rs = pstmt.executeQuery();
            List<Product> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapProduct(rs));
            }
            DBManager.closeConnection(conn);
            return result;
        } catch (SQLException e) {
            System.out.println("Error finding products by brand: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}