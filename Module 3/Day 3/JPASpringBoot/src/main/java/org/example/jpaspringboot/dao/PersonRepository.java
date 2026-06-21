package org.example.jpaspringboot.dao;

import org.example.jpaspringboot.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
