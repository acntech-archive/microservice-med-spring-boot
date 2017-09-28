package no.acntech.converter;

import org.springframework.stereotype.Component;

import no.acntech.employee.domain.Employee;
import no.acntech.resources.EmployeeDto;

@Component
public class EmployeeDtoConverter extends AbstractConverter<Employee, EmployeeDto> {

    @Override
    public EmployeeDto convert(Employee source) {
        return new EmployeeDto(source.getId(), source.getFirstName(), source.getLastName(), source.getDateOfBirth());
    }
}
