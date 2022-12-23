package ru.art3m1y.petprojectshop.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.models.RefreshToken;

import java.sql.Ref;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsById(long id);
    boolean existsByPerson(Person person);
    void deleteByPerson(Person person);
    Optional<RefreshToken> findByPerson(Person person);
}
