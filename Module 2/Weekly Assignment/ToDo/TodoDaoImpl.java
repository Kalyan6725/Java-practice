import java.util.ArrayList;
import java.util.List;

public class TodoDaoImpl implements TodoDao {

    private List<Todo> todos = new ArrayList<>();

    @Override
    public void addTodo(Todo todo) {
        todos.add(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return todos;
    }

    @Override
    public Todo getTodoById(int id) {
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }

    @Override
    public void updateTodo(Todo todo) {
        for (Todo t : todos) {
            if (t.getId() == todo.getId()) {
                t.setCompleted(todo.isCompleted());
                break;
            }
        }
    }

    @Override
    public void deleteTodo(int id) {
        todos.removeIf(todo -> todo.getId() == id);
    }
}