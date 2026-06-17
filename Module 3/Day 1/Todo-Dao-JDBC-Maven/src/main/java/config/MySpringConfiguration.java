package config;
import dao.TodoDao;
import dao.TodoDaoImpl;
import dao.TodoDaoImpl;
import UI.TodoConsoleController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import java.util.Scanner;
@Configuration
@ComponentScan(basePackages = {"UI", "dao", "config","entity","connection"})
public class MySpringConfiguration {
//    @Bean
//    public TodoDao todoDao(){
//        return new TodoDaoImpl();
//    }
   @Bean
   public Scanner scanner(){
       return new Scanner(System.in);
   }
//    @Bean
//    public TodoConsoleController consoleController(Scanner scanner,TodoDao todoDao){
//        return new TodoConsoleController(scanner,todoDao);
//    }
}