package org.northernarc.jpaspringboot_1_to_many.repository;

import org.northernarc.jpaspringboot_1_to_many.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
