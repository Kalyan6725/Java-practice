package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan({"dao","ui"})
public class MySpringConfiguration {
    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }

}
