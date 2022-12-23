package ru.art3m1y.petprojectshop.login.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.repositories.PersonRepository;
import ru.art3m1y.petprojectshop.login.security.PersonDetails;

import java.util.Optional;
@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Неверное почта или пароль");
        }

        return new PersonDetails(person.get());
    }
}
