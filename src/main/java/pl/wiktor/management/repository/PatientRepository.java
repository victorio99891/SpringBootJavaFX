package pl.wiktor.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.management.entity.PatientEntity;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    List<PatientEntity> findByFirstNameContainingIgnoringCase(String searchValue);

    List<PatientEntity> findByLastNameContainingIgnoringCase(String searchValue);

    List<PatientEntity> findByPeselContainingIgnoringCase(String searchValue);

    List<PatientEntity> findByWomen(Boolean isWoman);

    boolean existsByPesel(String pesel);
}
