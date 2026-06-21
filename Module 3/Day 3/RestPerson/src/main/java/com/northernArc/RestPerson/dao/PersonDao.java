package com.northernArc.RestPerson.dao;

import com.northernArc.RestPerson.entity.Person;

import java.util.List;

public interface PersonDao {
    void addPerson(Person person);
    Person getPersonById(int id);
    void updatePerson(int id, Person person);
    void deletePerson(int id);
    List<Person> getAllPersons();
}
