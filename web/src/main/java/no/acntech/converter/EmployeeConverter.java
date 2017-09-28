package no.acntech.converter;

import org.springframework.stereotype.Component;

import no.acntech.employee.domain.Employee;
import no.acntech.resources.EmployeeDto;

@Component
public class EmployeeConverter extends AbstractConverter<EmployeeDto, Employee> {

    @Override
    public Employee convert(EmployeeDto source) {
        return new Employee(source.getFirstName(), source.getLastName(), source.getDateOfBirth());
    }
}
