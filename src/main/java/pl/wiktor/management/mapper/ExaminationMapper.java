package pl.wiktor.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.wiktor.management.entity.ExaminationEntity;
import pl.wiktor.management.model.ExaminationBO;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, ImgTechMapper.class})
public interface ExaminationMapper {

    @Mapping(source = "imgTechBO", target = "imagingTechniqueEntity")
    @Mapping(source = "patientBO", target = "patientEntity")
    ExaminationEntity fromBOToEntity(ExaminationBO examinationBO);

    @Mapping(source = "imagingTechniqueEntity", target = "imgTechBO")
    @Mapping(source = "patientEntity", target = "patientBO")
    ExaminationBO fromEntityToBO(ExaminationEntity examinationEntity);

}
