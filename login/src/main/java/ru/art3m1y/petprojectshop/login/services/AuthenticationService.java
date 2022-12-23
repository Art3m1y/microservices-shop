package ru.art3m1y.petprojectshop.login.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.repositories.PersonRepository;
import ru.art3m1y.petprojectshop.login.utils.validators.exceptions.UserDoesNotExistException;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final PersonRepository personRepository;
    private final MailSenderService mailSenderService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PersonRepository personRepository, MailSenderService mailSenderService, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.mailSenderService = mailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void restorePassword(String email) {
        Person person = findByEmail(email);

        if (findByEmail(email) == null) {
            throw new UserDoesNotExistException();
        }

        //Send password restore email
        String uuid = UUID.randomUUID().toString();

        person.setRestorePasswordCode(uuid);

        String message = String.format("Hello, %s! Please visit this link to restore your password: %s", person.getName(), "http://localhost:7574/auth/restorepassword/" + uuid);

        mailSenderService.sendMessage(email, "Restore password", message);
    }

    public boolean existsByRestorePasswordCode(String restorePasswordCode) {
        return personRepository.existsByRestorePasswordCode(restorePasswordCode);
    }

    @Transactional
    public void successRestorePassword(String restorePasswordCode, String password) {
        Person person = personRepository.findByRestorePasswordCode(restorePasswordCode).orElse(null);

        person.setPassword(passwordEncoder.encode(password));
        person.setActivationCode(null);
        person.setRestorePasswordCode(null);

        personRepository.save(person);
    }
}
