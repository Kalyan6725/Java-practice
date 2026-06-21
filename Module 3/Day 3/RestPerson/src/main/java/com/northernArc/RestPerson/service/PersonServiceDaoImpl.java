package com.northernArc.RestPerson.service;
import com.northernArc.RestPerson.dao.PersonDao;
import com.northernArc.RestPerson.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PersonServiceDaoImpl implements PersonServiceDao{
    @Autowired
    private PersonDao personDao;

    @Override
    public void addPerson(Person person) {
        personDao.addPerson(person);
    }

    @Override
    public Person getPersonById(int id) {
        return personDao.getPersonById(id);
    }

    @Override
    public void updatePerson(int id, Person person) {
        personDao.updatePerson(id, person);
    }

    @Override
    public void deletePerson(int id) {
        personDao.deletePerson(id);
    }

    @Override
    public List<Person> getAllPersons() {
        return personDao.getAllPersons();
    }
}
