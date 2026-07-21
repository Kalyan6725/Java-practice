package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Weather;

import java.util.List;

public interface WeatherDao {
    public Weather create(Weather weather);
    public List<Weather> getAll();
    public Weather getById(int id);
    public void delete(int id);
}
