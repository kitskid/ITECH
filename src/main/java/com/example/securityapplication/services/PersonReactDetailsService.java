package com.example.securityapplication.services;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.PersonReactRepository;
import com.example.securityapplication.security.PersonReactDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PersonReactDetailsService implements UserDetailsService {

    private final PersonReactRepository personReactRepository;

    public PersonReactDetailsService(PersonReactRepository personReactRepository) {
        this.personReactRepository = personReactRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PersonReact> person = personReactRepository.findByLogin(username);
        if (person.isEmpty()){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new PersonReactDetails(person.get());
    }



}
