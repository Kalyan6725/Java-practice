package org.northernarc.jpaspringboot_1to1_mapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<org.northernarc.jpaspringboot_1to1_mapping.entity.Person, Integer> {
}
