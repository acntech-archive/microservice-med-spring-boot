package no.acntech.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
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

@Api(value = "employee")
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

    @ApiOperation(value = "Find all employees", response = EmployeeDto.class)
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> findAll() {
        final List<EmployeeDto> employees = Arrays.asList(conversionService.convert(employeeService.findAll(), EmployeeDto[].class));
        return ResponseEntity.ok(employees);
    }

    @ApiOperation(value = "Find employee by id", response = EmployeeDto.class)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "Employee id", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee was found"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable final Long id) {
        final Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            final EmployeeDto employeeDto = conversionService.convert(employee.get(), EmployeeDto.class);
            return ResponseEntity.ok(employeeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Create new employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee was created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Long> post(@RequestBody final EmployeeDto employeeDto, UriComponentsBuilder uri) {
        final Employee employee = conversionService.convert(employeeDto, Employee.class);
        final Long id = employeeService.save(employee).getId();
        final URI path = uri.path("employee/" + id).build().toUri();
        return ResponseEntity.created(path).build();
    }

    @ApiOperation(value = "Delete employee")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "Employee id", paramType = "path")
    })
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable final Long id) {
        employeeService.delete(id);
    }
}
