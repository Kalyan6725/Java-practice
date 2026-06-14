import java.util.List;

public interface TodoDao {

    public Todo getTodoById(int id);
    public List<Todo> getAllTodos();
    public void addTodo(Todo todo);
    public void updateTodo(Todo todo);
    public void deleteTodo(int id);
}