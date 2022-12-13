package com.example.securityapplication.repositories;


import com.example.securityapplication.models.PersonReact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonReactRepository extends JpaRepository<PersonReact, Integer> {
    Optional<PersonReact> findByLogin(String login);



    @Override
    void deleteById(Integer integer);
}
