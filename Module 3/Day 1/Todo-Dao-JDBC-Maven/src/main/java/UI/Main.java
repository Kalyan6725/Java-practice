package UI;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import config.MySpringConfiguration;
import dao.TodoDao;
import dao.TodoDaoImpl;
import entity.Todo;

import java.util.Scanner;
class Main {
    public static void main(String[] args) {
        // TodoDao todoDao = new TodoDaoImpl();
        ApplicationContext context = new AnnotationConfigApplicationContext(MySpringConfiguration.class);
        TodoDao todoDao = context.getBean(TodoDao.class);
        TodoConsoleController todoConsoleController = context.getBean(TodoConsoleController.class);
        todoConsoleController.printWelcomeMessage();
        todoConsoleController.showMenu();
    }
}