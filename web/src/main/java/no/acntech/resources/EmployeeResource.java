package no.acntech.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import no.acntech.employee.domain.Employee;
import no.acntech.employee.service.employee.EmployeeService;

@RestController
@RequestMapping("employee")
public class EmployeeResource {

    private final EmployeeService employeeService;
    private final ConversionService conversionService;

    @Autowired
    public EmployeeResource(EmployeeService employeeService, ConversionService conversionService) {
        this.employeeService = employeeService;
        this.conversionService = conversionService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> findAll() {
        final List<EmployeeDto> employees = Arrays.asList(conversionService.convert(employeeService.findAll(), EmployeeDto[].class));
        return ResponseEntity.ok(employees);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> findById(@PathVariable final Long id) {
        final Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            final EmployeeDto employeeDto = conversionService.convert(employee.get(), EmployeeDto.class);
            return ResponseEntity.ok(employeeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> save(@RequestBody final EmployeeDto employeeDto, UriComponentsBuilder uri) {
        final Employee employee = conversionService.convert(employeeDto, Employee.class);
        final Long id = employeeService.save(employee).getId();
        final URI path = uri.path("employee/" + id).build().toUri();
        return ResponseEntity.created(path).build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable final Long id) {
        final Optional<Employee> employee = employeeService.findById(id);

        if (employee.isPresent()) {
            employeeService.delete(employee.get().getId());
        }
    }
}
