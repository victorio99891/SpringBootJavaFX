package pl.wiktor.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.management.entity.ImagingTechniqueEntity;

@Repository
public interface ImgTechRepository extends JpaRepository<ImagingTechniqueEntity, Long> {
}
