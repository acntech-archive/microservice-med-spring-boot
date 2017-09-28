package no.acntech.employee.service.employee;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.employee.domain.Employee;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Long save() {
        final Employee entity = new Employee("John", "Doe", LocalDate.of(1986, Month.MAY, 31));
        return employeeRepository.save(entity).getId();
    }
}
