package com.example.securityapplication.security;


import com.example.securityapplication.models.PersonReact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonReactDetails implements UserDetails {

    private final PersonReact personReact;
    @Autowired
    public PersonReactDetails(PersonReact personReact){
        this.personReact = personReact;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(personReact.getRole()));
    }

    @Override
    public String getPassword() {
        return this.personReact.getPassword();
    }

    @Override
    public String getUsername() {
        return this.personReact.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public PersonReact getPerson(){
        return this.personReact;
    }
}
