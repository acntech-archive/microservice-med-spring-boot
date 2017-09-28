package no.acntech.employee.service.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import no.acntech.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
