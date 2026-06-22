package org.northernarc.jpaspringbootproject.repository;

import org.northernarc.jpaspringbootproject.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
