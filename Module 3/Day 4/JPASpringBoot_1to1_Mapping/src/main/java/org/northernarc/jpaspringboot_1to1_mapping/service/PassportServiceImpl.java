package org.northernarc.jpaspringboot_1to1_mapping.service;

import org.northernarc.jpaspringboot_1to1_mapping.entity.Passport;
import org.northernarc.jpaspringboot_1to1_mapping.entity.Person;
import org.northernarc.jpaspringboot_1to1_mapping.repository.PassportRepository;
import org.northernarc.jpaspringboot_1to1_mapping.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassportServiceImpl implements PassportServiceDao {
    @Autowired
    private PassportRepository passportRepository;
    @Autowired
    private PersonRepository personRepository;
    @Override
    public Passport addPassport(Passport passport){
        Person person=passport.getPerson();
        personRepository.save(person);
        return passportRepository.save(passport);
    }
    @Override
    public List<Passport> getAll(){
        return passportRepository.findAll();
    }
}