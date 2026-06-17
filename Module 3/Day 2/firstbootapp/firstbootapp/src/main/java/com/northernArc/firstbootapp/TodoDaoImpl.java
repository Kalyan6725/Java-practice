package com.northernArc.firstbootapp;

import com.northernArc.firstbootapp.Todo;
import com.northernArc.firstbootapp.TodoDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TodoDaoImpl implements TodoDao {
    private Map<Integer,Todo> todoMap=new HashMap<>();
    @Override
    public void save(Todo todo) {
        todoMap.put(todo.getId(),todo);
    }

    @Override
    public Map<Integer, Todo> findAll() {
        return todoMap;
    }

    @Override
    public void deleteById(int id) {
        todoMap.remove(id);
    }
}