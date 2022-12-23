package ru.art3m1y.petprojectshop.login.utils.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.art3m1y.petprojectshop.login.models.Person;
import ru.art3m1y.petprojectshop.login.services.RegistrationService;

@Component
public class RegistrationValidator implements Validator {
    private final RegistrationService registrationService;

    public RegistrationValidator(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (registrationService.findByEmail(person.getEmail())) {
            errors.rejectValue("email", "", "Пользователь с такой почтой уже существует");
        }
    }
}
