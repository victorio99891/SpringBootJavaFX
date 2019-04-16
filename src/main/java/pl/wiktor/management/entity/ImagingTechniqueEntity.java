package pl.wiktor.management.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "IMAGING_TECHNIQUES")
public class ImagingTechniqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMG_TECH_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

}
