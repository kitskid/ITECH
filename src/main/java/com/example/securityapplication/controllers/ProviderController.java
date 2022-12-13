package com.example.securityapplication.controllers;



import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.models.Provider;
import com.example.securityapplication.repositories.ProviderRepository;
import com.example.securityapplication.response.AddUserResponse;
import com.example.securityapplication.response.LoginResponse;
import com.example.securityapplication.services.PersonReactRegistrationService;
import com.example.securityapplication.services.ProviderService;
import com.example.securityapplication.util.ProviderValidator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class ProviderController {

    private final ProviderRepository providerRepository;
    private final ProviderService providerService;
    private final ProviderValidator providerValidator;
    private final PersonReactRegistrationService personReactRegistrationService;

    @Value("${upload.path}")
    private String uploadPath;

    public ProviderController(ProviderRepository providerRepository, ProviderService providerService, ProviderValidator providerValidator, PersonReactRegistrationService personReactRegistrationService) {
        this.providerRepository = providerRepository;
        this.providerService = providerService;
        this.providerValidator = providerValidator;
        this.personReactRegistrationService = personReactRegistrationService;
    }

    @GetMapping("/main/api/providers")
    public List<Provider> getProvidersAdmin() {
        return providerRepository.findAll();
    }

    @GetMapping("/main/api/providers/{login}")
    public ResponseEntity<?> findIdProviderByLogin(@PathVariable String login){
        Optional<Provider> provider = providerRepository.findByLogin(login);
        LoginResponse loginResponse = new LoginResponse();
        if(provider.isEmpty()){
            loginResponse.setMessage("Продавец не определен системой");
            return new ResponseEntity<>(loginResponse, HttpStatus.NOT_ACCEPTABLE);
        } else {
            loginResponse.setId(provider.get().getId());
            return ResponseEntity.ok(loginResponse);
        }
    }

    @DeleteMapping("/main/api/provider/delete/{id}")
    public ResponseEntity<?> deleteProviderByIdAdmin(@PathVariable int id){
        Optional<Provider> provider = providerService.findById(id);
        AddUserResponse response = new AddUserResponse();
        if (provider.isEmpty()){
            response.setMessage("Продавец не определен системой");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else {
            providerService.deleteById(id);
            response.setMessage("Продавец удален");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/main/api/provider/update")
    public ResponseEntity<?> postProviderAdmin(@RequestBody @Valid Provider provider, BindingResult bindingResult){

        AddUserResponse response = new AddUserResponse();
        providerValidator.validate(provider, bindingResult);
        if(bindingResult.hasErrors()){

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors ) {
                switch (error.getField()){
                    case "password" :
                        response.setPassword(error.getDefaultMessage());
                        break;
                    case "login" :
                        response.setLogin(error.getDefaultMessage());
                        break;
                    case "email" :
                        response.setEmail(error.getDefaultMessage());
                        break;
                    default:
                        response.setMessage("Неизвестная ошибка на стороне сервера");
                }
            }
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<Provider> providerBase = providerService.findById(provider.getId());
        if (providerBase.isEmpty()){
            response.setMessage("Продавец не определен системой");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else
            providerBase.get().setLogin(provider.getLogin());
            providerBase.get().setEmail(provider.getEmail());
            providerBase.get().setPhone(provider.getPhone());
            providerBase.get().setPassword(provider.getPassword());
            providerBase.get().setRole(provider.getRole());

        providerService.save(providerBase.get());

        response.setMessage("Серевер сохранил Продавца");
        return ResponseEntity.ok(response);
    }

    @PostMapping("main/api/provider/add")
    public ResponseEntity<?> addProviderAdmin(@RequestBody @Valid Provider provider, BindingResult bindingResult){

        AddUserResponse response = new AddUserResponse();
        providerValidator.validate(provider, bindingResult);
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors ) {
                switch (error.getField()){
                    case "password" :
                        response.setPassword(error.getDefaultMessage());
                        break;
                    case "login" :
                        response.setLogin(error.getDefaultMessage());
                        break;
                    case "email" :
                        response.setEmail(error.getDefaultMessage());
                        break;
                    default:
                        response.setMessage("Неизвестная ошибка на стороне сервера");
                }
            }
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }

        PersonReact personReact = new PersonReact();
        personReact.setLogin(provider.getLogin());
        personReact.setEmail(provider.getEmail());
        personReact.setPhone(provider.getPhone());
        personReact.setPassword(provider.getPassword());
        personReactRegistrationService.save(personReact);
        personReact.setRole("ROLE_PROVIDER");

        providerService.save(provider);

        response.setMessage("Серевер сохранил продавца");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/main/api/provider/avatar")
    public ResponseEntity<?> postProviderAvatarAdmin(@RequestParam("file") MultipartFile file, @RequestParam("login") String login) throws IOException {

        AddUserResponse response = new AddUserResponse();
        Optional<Provider> provider = providerService.findByLogin(login);
        if (provider.isEmpty()){
            response.setMessage("Покупатель не определен системой");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else {
            if (file != null) {
                File uploadDir = new File(uploadPath + "/provider");
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "_" + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/provider/" + resultFileName));

                provider.get().setFileName(resultFileName);
                providerService.save(provider.get());

                response.setMessage("Серевер сохранил аватар");
                System.out.println("Cервер установил у " + provider.get().getLogin() + " путь: " + provider.get().getFileName());
                return ResponseEntity.ok(response);
            } else {
                response.setMessage("Неизвестная ошибка на стороне сервера");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @GetMapping(value = "/main/api/provider/image/get/{filename:.+}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageProvider(@PathVariable String filename) throws IOException {

        boolean isFileExist = false;
        Path pathFile = Paths.get(uploadPath+ "/provider/avatar.png");
        if(Files.exists(pathFile)) isFileExist = true;

        if(filename.isEmpty() && !isFileExist) {
            return null;
        }
        if(filename.isEmpty() && isFileExist){
            InputStream infile = new FileInputStream(pathFile.toString());
            return IOUtils.toByteArray(infile);
        }
        if(!filename.isEmpty()) {
            Path path = Paths.get(uploadPath + "/provider/" + filename);
            if(Files.exists(path)){
                InputStream infile = new FileInputStream(path.toString());
                return IOUtils.toByteArray(infile);
            } else {
                if(isFileExist) {
                    InputStream infile = new FileInputStream(pathFile.toString());
                    return IOUtils.toByteArray(infile);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}



