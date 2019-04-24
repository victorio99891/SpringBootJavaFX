package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.entity.PatientEntity;
import pl.wiktor.management.mapper.PatientMapper;
import pl.wiktor.management.model.PatientBO;
import pl.wiktor.management.repository.PatientRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientBO> findAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::fromEntityToBO).collect(Collectors.toList());
    }

    public List<PatientBO> findByParameter(String parameter, String searchValue) {
        Optional<PatientEntity> patientEntity;

        if (parameter.equals("ID")) {
            patientEntity = patientRepository.findById(Long.valueOf(searchValue));
            return patientEntity.map(patientEntity1 -> Collections.singletonList(patientMapper.fromEntityToBO(patientEntity1))).orElse(Collections.emptyList());
        } else if (parameter.equals("FIRSTNAME")) {
            return patientRepository.findByFirstNameContainingIgnoringCase(searchValue).stream().map(patientMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("LASTNAME")) {
            return patientRepository.findByLastNameContainingIgnoringCase(searchValue).stream().map(patientMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("PESEL")) {
            return patientRepository.findByPeselContainingIgnoringCase(searchValue).stream().map(patientMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("GENDER")) {
            if (searchValue.equalsIgnoreCase("FEMALE")) {
                return patientRepository.findByWomen(true).stream().map(patientMapper::fromEntityToBO).collect(Collectors.toList());
            } else {
                return patientRepository.findByWomen(false).stream().map(patientMapper::fromEntityToBO).collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }

    public boolean checkIfPatientExist(String pesel) {
        return patientRepository.existsByPesel(pesel);
    }

    public void savePatient(PatientBO patientBO) {
        patientRepository.save(patientMapper.fromBOToEntity(patientBO));
    }

    public PatientBO findByPesel(String pesel) {
        return patientMapper.fromEntityToBO(patientRepository.findByPesel(pesel).get());
    }
}
