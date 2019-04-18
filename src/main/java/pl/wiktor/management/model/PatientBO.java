package pl.wiktor.management.model;

import lombok.Data;

@Data
public class PatientBO {
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private boolean women;
}
