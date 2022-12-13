package com.example.securityapplication.controllers;

import com.example.securityapplication.config.JWTTokenHelper;
import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.request.AuthenticationRequest;
import com.example.securityapplication.response.LoginResponse;
import com.example.securityapplication.security.PersonReactDetails;
import com.example.securityapplication.services.PersonReactRegistrationService;
import com.example.securityapplication.util.PersonReactValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/main/api")
@CrossOrigin
public class AuthenticationReactController {


    private AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenHelper jWTTokenHelper;
    private final PersonReactValidator personReactValidator;
    private final PersonReactRegistrationService personReactRegistrationService;

    @Autowired
    public AuthenticationReactController(AuthenticationManager authenticationManager, PersonReactValidator personReactValidator, PersonReactRegistrationService personReactRegistrationService) {
        this.authenticationManager = authenticationManager;
        this.personReactValidator = personReactValidator;
        this.personReactRegistrationService = personReactRegistrationService;
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> postUser(@RequestBody AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUserName(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        PersonReactDetails user=(PersonReactDetails) authentication.getPrincipal();
        System.out.println(user.getAuthorities().toString());
        String jwtToken = jWTTokenHelper.generateToken(user.getPerson().getLogin());

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setRole(user.getAuthorities().toString());
        response.setLogin(user.getUsername());
        System.out.println(jwtToken);
        //return  new ResponseEntity<>(response, HttpStatus.OK);
        try {
           return ResponseEntity.ok(response);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") PersonReact person){
        return "registration/registration";
    }


    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid PersonReact person, BindingResult bindingResult){
        personReactValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            System.out.println("Нашли ошибки: " + bindingResult.getAllErrors());
            return "registration/registration";
        }
        personReactRegistrationService.register(person);
        return "redirect:/index";
    }
}



