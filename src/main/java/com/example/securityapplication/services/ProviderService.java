package com.example.securityapplication.services;
import com.example.securityapplication.models.Provider;
import com.example.securityapplication.repositories.ProviderRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    public ProviderService(ProviderRepository providerRepository, PasswordEncoder passwordEncoder) {
        this.providerRepository = providerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<Provider> findByLogin(String login) throws UsernameNotFoundException {
        return providerRepository.findByLogin(login);
    }
    public Optional<Provider> findById(Integer id){
        return providerRepository.findProviderById(id);
    }
    @Transactional
    public void save(Provider provider){
        provider.setPassword(passwordEncoder.encode(provider.getPassword()));
        provider.setRole("ROLE_PROVIDER");

        providerRepository.save(provider);
    }

    @Transactional
    public void deleteById(Integer id){
        providerRepository.deleteById(id);
    }

    public Provider findByName(Provider provider) throws UsernameNotFoundException {
        Optional<Provider> prod = providerRepository.findByLogin(provider.getLogin());
        return prod.orElse(null);
    }
}
