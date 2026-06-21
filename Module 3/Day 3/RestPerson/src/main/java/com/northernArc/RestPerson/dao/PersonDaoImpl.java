package com.northernArc.RestPerson.dao;

import com.northernArc.RestPerson.entity.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDaoImpl implements PersonDao {
    private List<Person> personList = new ArrayList<>();

    @Override
    public void addPerson(Person person) {
        personList.add(person);
    }

    @Override
    public Person getPersonById(int id) {
        for (Person person : personList) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    @Override
    public void updatePerson(int id, Person person) {
        for (int i = 0; i < personList.size(); i++) {
            if (personList.get(i).getId() == id) {
                personList.set(i, person);
                return;
            }
        }
    }

    @Override
    public void deletePerson(int id) {
        for (int i = 0; i < personList.size(); i++) {
            if (personList.get(i).getId() == id) {
                personList.remove(i);
                return;
            }
        }
    }

    @Override
    public List<Person> getAllPersons() {
        return new ArrayList<>(personList);
    }
}
