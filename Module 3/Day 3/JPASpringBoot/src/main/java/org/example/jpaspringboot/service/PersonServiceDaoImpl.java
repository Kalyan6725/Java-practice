package org.example.jpaspringboot.service;

import org.example.jpaspringboot.dao.PersonRepository;
import org.example.jpaspringboot.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class PersonServiceDaoImpl implements PersonServiceDao{
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void addPerson(Person person) {
        personRepository.save(person);
    }

    @Override
    public Person getPersonById(int id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updatePerson(int id, Person person) {
        Person existingPerson = personRepository.findById(id).orElse(null);
        if (existingPerson == null) {
            return false;
        }

        existingPerson.setName(person.getName());
        existingPerson.setAge(person.getAge());
        personRepository.save(existingPerson);
        return true;
    }

    @Override
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
}
