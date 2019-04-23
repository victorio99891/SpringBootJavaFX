package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.mapper.ExaminationMapper;
import pl.wiktor.management.model.ExaminationBO;
import pl.wiktor.management.repository.ExaminationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final ExaminationMapper examinationMapper;

    public ExaminationService(ExaminationRepository examinationRepository, ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.examinationMapper = examinationMapper;
    }

    public List<ExaminationBO> findAllExaminations() {
        return examinationRepository.findAll().stream().map(examinationMapper::fromEntityToBO).collect(Collectors.toList());
    }
}
