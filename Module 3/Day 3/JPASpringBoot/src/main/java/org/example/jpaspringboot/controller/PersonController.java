package org.example.jpaspringboot.controller;

import org.example.jpaspringboot.entity.Person;
import org.example.jpaspringboot.service.PersonServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonServiceDao personServiceDao;

    @GetMapping("")
    public ResponseEntity<List<Person>> getPersons() {
        return ResponseEntity.ok(personServiceDao.getAllPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
        Person person = personServiceDao.getPersonById(id);
        if (person != null) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/{name}/{age}/{email}")
    public ResponseEntity<String> updatePerson(@PathVariable("id") int id, @PathVariable("name") String name, @PathVariable("age") int age, @PathVariable("email") String email) {
        boolean updated = personServiceDao.updatePerson(id, new Person(name, age));
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Person updated successfully");
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personServiceDao.addPerson(person);
        return ResponseEntity.ok("Person added successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") int id) {
        personServiceDao.deletePerson(id);
        return ResponseEntity.ok("Person deleted successfully");
    }
}
