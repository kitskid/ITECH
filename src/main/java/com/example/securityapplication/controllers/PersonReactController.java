package com.example.securityapplication.controllers;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.PersonReactRepository;
import com.example.securityapplication.security.PersonReactDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main/api")
@CrossOrigin
public class PersonReactController {

    private PersonReactRepository personReactRepository;
    private PersonReactDetails personReactDetails;



    @GetMapping("/main/api/currentUserInfo")
    public PersonReact getUserInfo(){
        return (PersonReact) personReactDetails.getAuthorities();
    }
}
