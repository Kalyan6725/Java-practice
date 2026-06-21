package org.northernarc.jpaspringboot_1to1_mapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<org.northernarc.jpaspringboot_1to1_mapping.entity.Passport, Integer> {
}
