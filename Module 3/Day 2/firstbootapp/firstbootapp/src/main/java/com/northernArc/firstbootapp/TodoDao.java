package com.northernArc.firstbootapp;

import java.util.Map;

public interface TodoDao {
    public void save(Todo todo);
    public Map<Integer,Todo> findAll();
    public void deleteById(int id);
}
