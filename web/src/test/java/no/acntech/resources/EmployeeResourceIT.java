package no.acntech.resources;

import java.net.URI;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class EmployeeResourceIT {

    private static final String URI = "/employee";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testEmployeResourceOperations() {
        final EmployeeDto employee = new EmployeeDto(1L, "Jon", "Doe", LocalDate.now());

        // create employee
        final URI uriToCreatedEmployee = restTemplate.postForLocation(URI, employee);
        assertThat(uriToCreatedEmployee).isNotNull();

        // get employee
        final ResponseEntity<EmployeeDto> createdEmployeeResponse = restTemplate.getForEntity(uriToCreatedEmployee, EmployeeDto.class);
        final EmployeeDto createdEmployee = createdEmployeeResponse.getBody();
        assertThat(createdEmployeeResponse.getStatusCode()).isEqualTo(OK);
        assertThat(createdEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(createdEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(createdEmployee.getDateOfBirth()).isEqualTo(employee.getDateOfBirth());

        // get all employees
        ResponseEntity<EmployeeDto[]> allEmployeesRespone = restTemplate.getForEntity(URI, EmployeeDto[].class);
        assertThat(allEmployeesRespone.getStatusCode()).isEqualTo(OK);
        assertThat(allEmployeesRespone.getBody()).hasSize(1);

        // delete employee
        restTemplate.delete(uriToCreatedEmployee);

        // get all employees after delete
        allEmployeesRespone = restTemplate.getForEntity(URI, EmployeeDto[].class);
        assertThat(allEmployeesRespone.getStatusCode()).isEqualTo(OK);
        assertThat(allEmployeesRespone.getBody()).isEmpty();
    }
}