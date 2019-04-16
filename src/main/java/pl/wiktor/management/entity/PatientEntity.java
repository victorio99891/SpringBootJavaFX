package pl.wiktor.management.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PATIENTS")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PATIENT_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PESEL", nullable = false, unique = true)
    private String PESEL;

    @Column(name = "IS_WOMEN", nullable = false)
    private boolean women;


}
