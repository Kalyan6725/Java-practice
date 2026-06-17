package com.northernArc.firstbootapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;
@Component
public class MyConsoleController {
    @Autowired
    private Scanner scanner;
    @Autowired
    private TodoDao todoDao;

    public void Menu() {
        while (true) {
            System.out.println("Enter Choice");
            System.out.println("1.add Todo");
            System.out.println("2.get all todos");
            System.out.println("3.delete todo by id");
            int choice = scanner.nextInt();
            scanner.nextLine();
            redirectChoice(choice);
        }
    }

    void redirectChoice(int choice) {
        switch (choice){
            case 1:
                addTodo();
                break;
            case 2:
                System.out.println(todoDao.findAll());
                break;
            case 3:
                System.out.println("Enter Todo Id:");
                int id=scanner.nextInt();
                todoDao.deleteById(id);
                break;
        }
    }

    public void addTodo() {
        System.out.println("Enter Todo Details");
        System.out.println("Id:");
        int id=scanner.nextInt();
        scanner.nextLine();
        System.out.println("Content:");
        String content=scanner.nextLine();
        System.out.println("Status:");
        boolean status=scanner.nextLine().equalsIgnoreCase("true");
        Todo todo=new Todo(id,content,status);
        todoDao.save(todo);
    }
    
}
