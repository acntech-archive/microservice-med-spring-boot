package no.acntech.resources;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.acntech.EmployeeApplication;
import no.acntech.converter.EmployeeConverter;
import no.acntech.converter.EmployeeDtoConverter;
import no.acntech.employee.domain.Employee;
import no.acntech.employee.service.employee.EmployeeService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {EmployeeResource.class})
@ContextConfiguration(classes = {
        EmployeeApplication.class,
        EmployeeConverter.class,
        EmployeeDtoConverter.class
})
public class EmployeeResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getEmployee() throws Exception {
        final Employee john = new Employee("John", "Doe", LocalDate.now());
        when(employeeService.findById(1L)).thenReturn(Optional.of(john));

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmployeeNotFound() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllEmployees() throws Exception {
        final Employee john = new Employee("John", "Doe", LocalDate.now());
        final Employee jane = new Employee("Jane", "Doe", LocalDate.now());
        when(employeeService.findAll()).thenReturn(Arrays.asList(john, jane));

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk());
    }

    @Test
    public void saveEmployee() throws Exception {
        final EmployeeDto john = new EmployeeDto(1L, "John", "Doe", LocalDate.now());
        final Employee employee = new Employee(john.getFirstName(), john.getLastName(), john.getDateOfBirth());
        employee.setId(john.getId());

        when(employeeService.save(any(Employee.class))).thenReturn(employee);

        final String content = objectMapper.writeValueAsString(john);

        mockMvc.perform(
                post("/employee")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(employeeService).save(any(Employee.class));
    }

    @Test
    public void deleteEmployee() throws Exception {
        mockMvc.perform(
                delete("/employee/1"))
                .andExpect(status().isAccepted());

        verify(employeeService).delete(1L);
    }
}