package no.acntech.employee.service.employee;

import org.springframework.data.repository.CrudRepository;

import no.acntech.employee.domain.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
