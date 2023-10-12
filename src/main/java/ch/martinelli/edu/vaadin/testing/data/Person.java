package ch.martinelli.edu.vaadin.testing.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class Person extends AbstractEntity {

    private String firstName;
    private String lastName;
    @Email
    private String email;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
