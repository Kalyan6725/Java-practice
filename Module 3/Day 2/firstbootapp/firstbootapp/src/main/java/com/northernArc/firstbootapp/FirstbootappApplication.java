package com.northernArc.firstbootapp;

import com.northernArc.firstbootapp.MyConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("!test")
public class FirstbootappApplication implements CommandLineRunner {
	@Autowired
    MyConsoleController myConsoleController;

	public static void main(String[] args) {
		SpringApplication.run(FirstbootappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		myConsoleController.Menu();
	}

}
