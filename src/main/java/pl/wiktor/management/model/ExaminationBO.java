package pl.wiktor.management.model;

import lombok.Data;

@Data
public class ExaminationBO {

    private Long id;

    private String status;

    private String description;

    private ImgTechBO imgTechBO;

    private PatientBO patientBO;

}
