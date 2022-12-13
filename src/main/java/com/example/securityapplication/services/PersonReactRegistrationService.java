package com.example.securityapplication.services;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.PersonReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonReactRegistrationService {
    private final PersonReactRepository personReactRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonReactRegistrationService(PersonReactRepository personReactRepository, PasswordEncoder passwordEncoder) {
        this.personReactRepository = personReactRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PersonReact findByLogin(PersonReact person) throws UsernameNotFoundException {
        Optional<PersonReact> user = personReactRepository.findByLogin(person.getLogin());
        return user.orElse(null);
    }
    @Transactional
    public void register(PersonReact person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personReactRepository.save(person);
        System.out.println("Успешно сохранили пользователя");
    }
    @Transactional
    public void save(PersonReact person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personReactRepository.save(person);
        System.out.println("Успешно сохранили пользователя");
    }
    @Transactional
    public void deleteById(int id){
        personReactRepository.deleteById(id);
    }
}
