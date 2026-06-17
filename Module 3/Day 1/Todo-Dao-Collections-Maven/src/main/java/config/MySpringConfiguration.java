package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dao.TodoDao;
import dao.TodoDaoImpl;

@Configuration
public class MySpringConfiguration {

    @Bean
    public TodoDao todoDao() {
        return new TodoDaoImpl();
    }
}
