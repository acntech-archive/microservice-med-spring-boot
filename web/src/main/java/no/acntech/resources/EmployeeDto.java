package no.acntech.resources;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EmployeeDto {

    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    @JsonCreator
    public EmployeeDto(@JsonProperty("firstName") String firstName,
                       @JsonProperty("lastName") String lastName,
                       @JsonProperty("dateOfBirth") LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    @JsonGetter("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonGetter("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonGetter("dateOfBirth")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
