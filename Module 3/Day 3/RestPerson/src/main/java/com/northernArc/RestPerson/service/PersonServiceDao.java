package com.northernArc.RestPerson.service;
import com.northernArc.RestPerson.entity.Person;
import java.util.List;

public interface PersonServiceDao {
    void addPerson(Person person);
    Person getPersonById(int id);
    void updatePerson(int id, Person person);
    void deletePerson(int id);
    List<Person> getAllPersons();
}
