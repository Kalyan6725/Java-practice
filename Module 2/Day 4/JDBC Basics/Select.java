import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Select {
    public static void main(String[] args) throws Exception {

        Class.forName("org.postgresql.Driver");

        try(Connection conn =
            DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/practice",
                "postgres",
                "12345"
            )){
                System.out.println("Connected");
                String sql = "SELECT * FROM car where price > 50000";
                System.out.println(sql);
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getString("category") + " " + rs.getInt("price"));
                }

            }catch(SQLException e){
                System.out.println(e);
            }
    }
}