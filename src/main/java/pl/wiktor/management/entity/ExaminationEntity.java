package pl.wiktor.management.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "EXAMINATIONS")
public class ExaminationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXAMINATION_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "IMG_TECH_ID")
    private ImagingTechniqueEntity imagingTechniqueEntity;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "PATIENT_ID")
    private PatientEntity patientEntity;
}
