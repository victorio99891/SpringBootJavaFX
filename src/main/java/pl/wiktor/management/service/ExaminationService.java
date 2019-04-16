package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.repository.ExaminationRepository;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;

    public ExaminationService(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }
}
