package com.example.securityapplication.util;

import com.example.securityapplication.models.Product;
import com.example.securityapplication.models.Provider;
import com.example.securityapplication.services.ProviderService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProviderValidator implements Validator {

    private final ProviderService providerService;

    public ProviderValidator(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Provider provider = (Provider) target;

        if(providerService.findByName(provider) != null){
            errors.rejectValue("name", "", "Данное имя товара уже занято");
        }
    }
}
