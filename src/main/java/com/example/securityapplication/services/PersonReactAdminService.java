package com.example.securityapplication.services;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.PersonReactRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


public class PersonReactAdminService {
    private final PersonReactRepository personReactRepository;

    public PersonReactAdminService(PersonReactRepository personReactRepository) {
        this.personReactRepository = personReactRepository;
    }

    public PersonReact findByLog(String login){
        Optional<PersonReact> user = personReactRepository.findByLogin(login);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user.get();
    }
}
