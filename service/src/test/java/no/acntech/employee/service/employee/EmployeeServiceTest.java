package no.acntech.employee.service.employee;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.acntech.employee.domain.Employee;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void findById() throws Exception {
        final Employee john = new Employee("John", "Doe", LocalDate.now());
        when(employeeRepository.findOne(1L)).thenReturn(john);
        final Employee foundEmployee = employeeService.findById(1L).get();

        assertThat(foundEmployee.getFirstName(), is(equalTo(john.getFirstName())));
        assertThat(foundEmployee.getLastName(), is(equalTo(john.getLastName())));
        assertThat(foundEmployee.getDateOfBirth(), is(equalTo(john.getDateOfBirth())));
    }

    @Test
    public void findAll() throws Exception {
        final Employee john = new Employee("John", "Doe", LocalDate.now());
        final Employee jane = new Employee("Jane", "Doe", LocalDate.now());

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(john, jane));

        final List<Employee> allEmployees = employeeService.findAll();
        assertThat(allEmployees, hasSize(2));
        assertThat(allEmployees, containsInAnyOrder(john, jane));
    }

    @Test
    public void save() throws Exception {
        final Employee john = new Employee("John", "Doe", LocalDate.now());
        when(employeeRepository.save(john)).thenReturn(john);

        final Employee savedJohn = employeeService.save(john);
        assertThat(savedJohn.getFirstName(), is(equalTo(john.getFirstName())));
        assertThat(savedJohn.getLastName(), is(equalTo(john.getLastName())));
        assertThat(savedJohn.getDateOfBirth(), is(equalTo(john.getDateOfBirth())));
    }

    @Test
    public void deletePresent() throws Exception {
        final Employee john = new Employee("John", "Doe", LocalDate.now());
        john.setId(1L);

        when(employeeRepository.findOne(john.getId())).thenReturn(john);

        employeeService.delete(john.getId());
        verify(employeeRepository).delete(john.getId());
    }

    @Test
    public void deleteNotPresent() throws Exception {
        when(employeeRepository.findOne(1L)).thenReturn(null);
        employeeService.delete(1L);
        verify(employeeRepository).findOne(1L);
        verifyNoMoreInteractions(employeeRepository);
    }
}