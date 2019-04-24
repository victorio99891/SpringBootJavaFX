package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.entity.ExaminationEntity;
import pl.wiktor.management.entity.ImagingTechniqueEntity;
import pl.wiktor.management.entity.PatientEntity;
import pl.wiktor.management.mapper.ExaminationMapper;
import pl.wiktor.management.model.ExaminationBO;
import pl.wiktor.management.model.PatientBO;
import pl.wiktor.management.model.enums.ExaminationStatusEnum;
import pl.wiktor.management.repository.ExaminationRepository;
import pl.wiktor.management.repository.ImgTechRepository;
import pl.wiktor.management.repository.PatientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final PatientRepository patientRepository;
    private final ImgTechRepository imgTechRepository;
    private final ExaminationMapper examinationMapper;

    public ExaminationService(ExaminationRepository examinationRepository, PatientRepository patientRepository, ImgTechRepository imgTechRepository, ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.patientRepository = patientRepository;
        this.imgTechRepository = imgTechRepository;
        this.examinationMapper = examinationMapper;
    }

    public List<ExaminationBO> findAllExaminations() {
        return examinationRepository.findAll().stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
    }

    public void registerExamination(PatientBO patientToRegister, String examinationType) {
        Optional<PatientEntity> patientEntity = this.patientRepository.findByPesel(patientToRegister.getPesel());
        Optional<ImagingTechniqueEntity> imagingTechniqueEntity = this.imgTechRepository.findByNameIgnoringCase(examinationType);
        if (patientEntity.isPresent() && imagingTechniqueEntity.isPresent()) {
            ExaminationEntity examinationEntity = new ExaminationEntity();
            examinationEntity.setStatus(ExaminationStatusEnum.REGISTERED.getDisplayName());
            examinationEntity.setPatientEntity(patientEntity.get());
            examinationEntity.setImagingTechniqueEntity(imagingTechniqueEntity.get());
            examinationRepository.save(examinationEntity);
        }
    }
}
