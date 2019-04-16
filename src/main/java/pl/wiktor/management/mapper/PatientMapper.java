package pl.wiktor.management.mapper;

import org.mapstruct.Mapper;
import pl.wiktor.management.entity.PatientEntity;
import pl.wiktor.management.model.PatientBO;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientEntity fromBOToEntity(PatientBO patientBO);

    PatientBO fromEntityToBO(PatientEntity patientEntity);
}
