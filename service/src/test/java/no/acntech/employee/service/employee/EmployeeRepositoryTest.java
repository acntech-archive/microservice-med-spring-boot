package no.acntech.employee.service.employee;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import no.acntech.employee.domain.Employee;
import no.acntech.employee.service.EmployeeDatabaseConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@DataJpaTest
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@Import({EmployeeDatabaseConfig.class})
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
@ContextConfiguration(classes = EmployeeRepository.class)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    public void saveEmployee() throws Exception {
        final Employee employee = new Employee("John", "Doe", LocalDate.of(1986, Month.MAY, 31));
        final Employee savedEmployee = repository.save(employee);

        assertThat(savedEmployee.getId(), is(notNullValue()));
        assertThat(savedEmployee.getFirstName(), is(equalTo(employee.getFirstName())));
        assertThat(savedEmployee.getLastName(), is(equalTo(employee.getLastName())));
        assertThat(savedEmployee.getDateOfBirth(), is(equalTo(employee.getDateOfBirth())));
    }
}