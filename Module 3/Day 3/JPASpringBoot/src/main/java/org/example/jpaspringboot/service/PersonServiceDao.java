package org.example.jpaspringboot.service;
import org.example.jpaspringboot.entity.Person;

import java.util.List;

public interface PersonServiceDao {
    void addPerson(Person person);
    Person getPersonById(int id);
    boolean updatePerson(int id, Person person);
    void deletePerson(int id);
    List<Person> getAllPersons();
}
