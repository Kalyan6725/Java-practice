package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.service.WeatherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherApiRestController {
    @Autowired
    WeatherDao weatherDao;

    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {
        return new ResponseEntity<>(weatherDao.create(weather), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Weather>> getAllWeather() {
        return new ResponseEntity<>(weatherDao.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weather> getById(@PathVariable int id) {
        Weather weather = weatherDao.getById(id);
        if (weather != null) {
            return new ResponseEntity<>(weather, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        if (weatherDao.getById(id) != null) {
            weatherDao.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
