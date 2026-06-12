import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Insert {
    public static void main(String[] args) throws Exception {

        Class.forName("org.postgresql.Driver");

        try(Connection conn =
            DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/practice",
                "postgres",
                "12345"
            )){
                System.out.println("Connected");
                String sql = "INSERT INTO car(name, category, price) VALUES('BMW', 'SUV', 50000),('Audi', 'Sedan', 40000),('Mercedes', 'SUV', 60000)";
                System.out.println(sql);
                PreparedStatement ps = conn.prepareStatement(sql);
                System.out.println(ps.executeUpdate());
                

            }catch(SQLException e){
                System.out.println(e);
            }
    }
}