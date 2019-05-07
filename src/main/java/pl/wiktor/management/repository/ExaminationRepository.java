package pl.wiktor.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.management.entity.ExaminationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExaminationRepository extends JpaRepository<ExaminationEntity, Long> {

    Optional<ExaminationEntity> findById(Long id);

    List<ExaminationEntity> findByPatientEntity_FirstNameContainingIgnoringCase(String name);

    List<ExaminationEntity> findByPatientEntity_LastNameContainingIgnoringCase(String name);

    List<ExaminationEntity> findByPatientEntity_PeselContainingIgnoringCase(String name);

    List<ExaminationEntity> findByImagingTechniqueEntity_NameContainingIgnoringCase(String name);

    List<ExaminationEntity> findByStatusContainingIgnoringCase(String name);


}
