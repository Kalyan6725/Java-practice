package UI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import config.MySpringConfiguration;
import dao.TodoDaoImpl;
import dao.TodoDao;
import entity.Todo;
import java.util.Scanner;


class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MySpringConfiguration.class);

        TodoConsoleController todoConsoleController = context.getBean(TodoConsoleController.class);
        todoConsoleController.printWelcomeMessage();
        todoConsoleController.showMenu();
    }
}
