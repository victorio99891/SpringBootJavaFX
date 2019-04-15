package pl.wiktor.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.management.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByEmailContainingIgnoringCase(String email);

    List<UserEntity> findByFirstNameContainingIgnoringCase(String name);

    List<UserEntity> findByLastNameContainingIgnoringCase(String lastName);

    List<UserEntity> findByRole_NameContainingIgnoringCase(String role);

}
