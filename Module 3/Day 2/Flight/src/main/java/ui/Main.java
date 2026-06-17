package ui;

import config.MySpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class Main {
    public static void main() throws SQLException {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(MySpringConfiguration.class);
        MyConsoleController myConsoleController=context.getBean(MyConsoleController.class);
        myConsoleController.Menu();
    }

}
