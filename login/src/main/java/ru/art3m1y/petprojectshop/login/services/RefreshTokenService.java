package ru.art3m1y.petprojectshop.login.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.models.RefreshToken;
import ru.art3m1y.petprojectshop.login.repositories.RefreshTokenRepository;

import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public boolean existsById(long id) {
       return refreshTokenRepository.existsById(id);
    }

    @Transactional
    public void deleteById(long id) {
        refreshTokenRepository.deleteById(id);
    }

    @Transactional
    public void updateRefreshToken(Person person, RefreshToken refreshTokenUpdated) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByPerson(person);

        if (refreshToken.isPresent()) {
            deleteById(refreshToken.get().getId());
        }

        save(refreshTokenUpdated);
    }

    @Transactional(readOnly = true)
    public RefreshToken findById(long id) {
        return refreshTokenRepository.findById(id).orElse(null);
    }
}
