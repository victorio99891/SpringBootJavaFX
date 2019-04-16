package pl.wiktor.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.management.entity.ExaminationEntity;

@Repository
public interface ExaminationRepository extends JpaRepository<ExaminationEntity, Long> {

}
