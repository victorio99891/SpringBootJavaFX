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

import java.util.ArrayList;
import java.util.Collections;
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

    public List<ExaminationBO> findByParameter(String parameter, String searchValue) {
        Optional<ExaminationEntity> examinationEntity;
        if (parameter.equals("ID")) {
            examinationEntity = examinationRepository.findById(Long.valueOf(searchValue));
            return examinationEntity.map(examination -> Collections.singletonList(examinationMapper.fromEntityToBO(examination))).orElse(Collections.emptyList());
        } else if (parameter.equals("FIRSTNAME")) {
            return examinationRepository.findByPatientEntity_FirstNameContainingIgnoringCase(searchValue).stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("LASTNAME")) {
            return examinationRepository.findByPatientEntity_LastNameContainingIgnoringCase(searchValue).stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("PESEL")) {
            return examinationRepository.findByPatientEntity_PeselContainingIgnoringCase(searchValue).stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("EXAMINATION")) {
            return examinationRepository.findByImagingTechniqueEntity_NameContainingIgnoringCase(searchValue).stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("STATUS")) {
            return examinationRepository.findByStatusContainingIgnoringCase(searchValue).stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public void deletePatient(ExaminationBO examinationBO) {
        examinationRepository.delete(examinationRepository.findById(examinationBO.getId()).get());
    }

    public void makeExamination(ExaminationBO examinationBO, boolean inProgress) {
        Optional<ExaminationEntity> examinationEntity = examinationRepository.findById(examinationBO.getId());
        if (examinationEntity.isPresent() && inProgress) {
            ExaminationEntity entity = examinationEntity.get();
            entity.setStatus(ExaminationStatusEnum.IN_PROGRESS.getDisplayName());
            examinationRepository.save(entity);
        } else if (examinationEntity.isPresent()) {
            ExaminationEntity entity = examinationEntity.get();
            entity.setStatus(ExaminationStatusEnum.FOR_DESCRIPTION.getDisplayName());
            examinationRepository.save(entity);
        }
    }

    public void saveDescription(ExaminationBO examinationToManage, String text) {
        Optional<ExaminationEntity> examinationEntity = examinationRepository.findById(examinationToManage.getId());
        if (examinationEntity.isPresent()) {
            ExaminationEntity examination = examinationEntity.get();
            examination.setDescription(text);
            examination.setStatus(ExaminationStatusEnum.DONE.getDisplayName());
            examinationRepository.save(examination);
        }
    }
}
