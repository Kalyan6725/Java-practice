package com.hackerrank.weather.service.impl;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import com.hackerrank.weather.service.WeatherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class weatherImpl implements WeatherDao {
    @Autowired
    WeatherRepository weatherRepository;

    @Override
    public Weather create(Weather weather) {
        return weatherRepository.save(weather);
    }

    @Override
    public List<Weather> getAll() {
        return weatherRepository.findAll();
    }

    @Override
    public Weather getById(int id) {
        return weatherRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(int id) {
        weatherRepository.deleteById(id);

    }
}
