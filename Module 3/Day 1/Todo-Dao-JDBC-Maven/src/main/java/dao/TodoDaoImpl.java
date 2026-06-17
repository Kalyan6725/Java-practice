package dao;
import connection.DBManager;
import entity.Todo;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class TodoDaoImpl implements TodoDao {
    public TodoDaoImpl() {
        String sql = "CREATE TABLE IF NOT EXISTS todos (id INT PRIMARY KEY, task VARCHAR(255), completed BOOLEAN)";
        try {
            Connection conn = DBManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void addTodo(Todo todo) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "INSERT INTO todos (id, task, completed) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, todo.getId());
            pstmt.setString(2, todo.getTask());
            pstmt.setBoolean(3, todo.isCompleted());
            pstmt.executeUpdate();
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM todos";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Todo todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setTask(rs.getString("task"));
                todo.setCompleted(rs.getBoolean("completed"));
                todos.add(todo);
            }
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return todos;
    }

    @Override
    public Todo getTodoById(int id) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "SELECT * FROM todos WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Todo todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setTask(rs.getString("task"));
                todo.setCompleted(rs.getBoolean("completed"));
                DBManager.closeConnection(conn);
                return todo;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void updateTodo(int id,Todo todo) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "UPDATE todos SET completed = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, todo.isCompleted());
            pstmt.setInt(2, id);
            System.out.println(pstmt.executeUpdate());
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void deleteTodo(int id) {
        try {
            Connection conn = DBManager.getConnection();
            String sql = "DELETE FROM todos WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DBManager.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}