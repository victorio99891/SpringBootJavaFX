package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.model.entity.QuestionEntity;
import pl.wiktor.management.repository.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionEntity> findAll() {
        return questionRepository.findAll();
    }
}
