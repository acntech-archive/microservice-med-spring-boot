package no.acntech.resources;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EmployeeDto {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    @JsonCreator
    public EmployeeDto(@JsonProperty("id") Long id,
                       @JsonProperty("firstName") String firstName,
                       @JsonProperty("lastName") String lastName,
                       @JsonProperty("dateOfBirth") LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    @JsonGetter("id")
    public Long getId() {
        return id;
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
