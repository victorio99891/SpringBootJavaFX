package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
}
