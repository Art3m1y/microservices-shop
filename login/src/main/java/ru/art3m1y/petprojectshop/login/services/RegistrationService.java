package ru.art3m1y.petprojectshop.login.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.repositories.PersonRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegistrationService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    @Transactional(readOnly = true)
    public boolean findByEmail(String email) {
        return personRepository.findByEmail(email).isPresent() ? true : false;
    }

    @Transactional()
    public void save(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setCreatedAt(new Date());
        person.setActivationCode(UUID.randomUUID().toString());
        personRepository.save(person);

        //Send activation code on email
        String link = "http://localhost:7574/auth/activate/" + person.getActivationCode();
        String message = String.format("Hello, %s! Please, visit this link to activate your account: %s", person.getName(), link);

        mailSenderService.sendMessage(person.getEmail(), "Activation code", message);
    }

    @Transactional(readOnly = true)
    public boolean existsByActivationCode(String activationCode) {
        return personRepository.existsByActivationCode(activationCode) ? true : false;
    }

    @Transactional
    public void activateByActivationCode(String activationCode) {
        Optional<Person> person = personRepository.findByActivationCode(activationCode);

        if (person.isPresent()) {
            person.get().setActivationCode(null);
            personRepository.save(person.get());
        }
    }
}
