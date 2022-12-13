package com.example.securityapplication.util;


import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.services.PersonReactRegistrationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PersonReactValidator implements Validator {

    private PersonReactRegistrationService personReactRegistrationService;

    public PersonReactValidator(PersonReactRegistrationService personReactRegistrationService) {
        this.personReactRegistrationService = personReactRegistrationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonReact.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonReact person = (PersonReact) target;

        if(personReactRegistrationService.findByLogin(person) != null){
            errors.rejectValue("login", "", "Данный логин уже занят");
        }

    }
}
