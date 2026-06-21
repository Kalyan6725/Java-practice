package org.northernarc.jpaspringbootbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<org.northernarc.jpaspringbootbook.entity.Book, Integer> {
}
