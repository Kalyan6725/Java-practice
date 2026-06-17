package UI;
import dao.TodoDaoImpl;
import dao.TodoDao;
import entity.Todo;
import java.util.Scanner;
class Main {
    public static void main(String[] args) {
        TodoDao todoDao = new TodoDaoImpl();
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Enter Choice:\n1. Add Todo \n2. Get All Todos \n3. Get Todo By ID \n4. Update Todo \n5. Delete Todo \n6. Exit");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter ID :");
                    int id = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    System.out.println("Enter Task:");
                    String task = sc.nextLine();
                    todoDao.addTodo(new Todo(id, task));
                    break;
                case 2:
                    System.out.println("All Todos:");
                    System.out.println(todoDao.getAllTodos());
                    break;
                case 3:
                    System.out.println("Enter ID:");
                    int searchId = sc.nextInt();
                    Todo foundTodo = todoDao.getTodoById(searchId);
                    if (foundTodo != null) {
                        System.out.println(foundTodo);
                    } else {
                        System.out.println("Todo not found.");
                    }
                    break;
                case 4:
                    System.out.println("Enter ID to update:");
                    int updateId = sc.nextInt();
                    Todo todoToUpdate = todoDao.getTodoById(updateId);
                    if (todoToUpdate != null) {
                        System.out.println("Enter new completion status (true/false):");
                        boolean completed = sc.nextBoolean();
                        todoToUpdate.setCompleted(completed);
                        todoDao.updateTodo(todoToUpdate);
                        System.out.println("Todo updated.");
                    } else {
                        System.out.println("Todo not found.");
                    }
                    break;
                case 5:
                    System.out.println("Enter ID to delete:");
                    int deleteId = sc.nextInt();
                    todoDao.deleteTodo(deleteId);
                    System.out.println("Todo deleted.");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }
}
