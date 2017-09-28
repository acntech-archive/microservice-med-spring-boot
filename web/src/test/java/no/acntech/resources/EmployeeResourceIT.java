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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.emptyArray;
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
        assertThat(uriToCreatedEmployee, notNullValue());

        // get employee
        final ResponseEntity<EmployeeDto> createdEmployeeResponse = restTemplate.getForEntity(uriToCreatedEmployee, EmployeeDto.class);
        final EmployeeDto createdEmployee = createdEmployeeResponse.getBody();
        assertThat(createdEmployeeResponse.getStatusCode(), is(OK));
        assertThat(createdEmployee.getFirstName(), is(employee.getFirstName()));
        assertThat(createdEmployee.getLastName(), is(employee.getLastName()));
        assertThat(createdEmployee.getDateOfBirth(), is(employee.getDateOfBirth()));

        // get all employees
        ResponseEntity<EmployeeDto[]> allEmployeesRespone = restTemplate.getForEntity(URI, EmployeeDto[].class);
        assertThat(allEmployeesRespone.getStatusCode(), is(OK));
        assertThat(allEmployeesRespone.getBody(), arrayWithSize(1));

        // delete employee
        restTemplate.delete(uriToCreatedEmployee);

        // get all employees after delete
        allEmployeesRespone = restTemplate.getForEntity(URI, EmployeeDto[].class);
        assertThat(allEmployeesRespone.getStatusCode(), is(OK));
        assertThat(allEmployeesRespone.getBody(), emptyArray());
    }
}