import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Delete {
    public static void main(String[] args) throws Exception {

        Class.forName("org.postgresql.Driver");

        try(Connection conn =
            DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/practice",
                "postgres",
                "12345"
            )){
                System.out.println("Connected");
                String sql = "DELETE FROM car WHERE name = 'BMW'";
                System.out.println(sql);
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
                

            }catch(SQLException e){
                System.out.println(e);
            }
    }
}