package org.northernarc.jpaspringbootproject.repository;

import org.northernarc.jpaspringbootproject.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
