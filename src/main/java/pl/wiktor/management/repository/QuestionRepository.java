package pl.wiktor.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.management.model.entity.QuestionEntity;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    Optional<QuestionEntity> findById(Long id);

}
