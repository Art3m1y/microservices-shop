package ru.art3m1y.petprojectshop.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.art3m1y.petprojectshop.login.models.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
    boolean existsByActivationCode(String activationCode);
    Optional<Person> findByActivationCode(String activationCode);
    Optional<Person> findByRestorePasswordCode(String restorePasswordCode);
    boolean existsByRestorePasswordCode(String restorePasswordCode);
}
