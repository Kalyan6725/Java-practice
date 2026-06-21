package org.northernarc.jpaspringboot_1_to_many.repository;

import org.northernarc.jpaspringboot_1_to_many.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
