package com.example.securityapplication.controllers;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.PersonReactRepository;
import com.example.securityapplication.response.AddUserResponse;
import com.example.securityapplication.services.PersonReactDetailsService;
import com.example.securityapplication.services.PersonReactRegistrationService;
import com.example.securityapplication.util.PersonReactValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//origins = "http://localhost:3000"
@RestController
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE})
public class MainController {


    private final PersonReactRepository personReactRepository;
    private final PersonReactValidator personReactValidator;
    private final PersonReactRegistrationService personReactRegistrationService;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(PersonReactRepository personReactRepository, PersonReactValidator personReactValidator, PersonReactRegistrationService personReactRegistrationService, PersonReactDetailsService personReactDetailsService) {

        this.personReactRepository = personReactRepository;
        this.personReactValidator = personReactValidator;
        this.personReactRegistrationService = personReactRegistrationService;

    }

    @GetMapping("/index")
    public String index() {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

//        System.out.println("ID пользователя" + personDetails.getPerson().getId());
//        System.out.println("Логин пользователя" + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя" + personDetails.getPerson().getPassword());
        // RestController restController = new RestController();
        // restController.requestMethod();
        return "index";
    }

    @GetMapping("/main")
    public String one() {
        System.out.println("Привет! Все работает!");

        return "main";
    }
    //@ModelAttribute
    //public void setResponseHeader(HttpServletResponse response) {
    //    response.setHeader("Access-Control-Allow-Origin", "*");

    //}


    @GetMapping("/main/api")
    public List<PersonReact> getUsers() {
        return personReactRepository.findAll();
    }

    @DeleteMapping("/main/api/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable int id){
        Optional<PersonReact> personReact = personReactRepository.findById(id);
        AddUserResponse response = new AddUserResponse();
        if (personReact.isEmpty()){
            response.setMessage("Пользователь не определен системой");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else {
            personReactRegistrationService.deleteById(id);
            response.setMessage("Пользователь удален");
            return ResponseEntity.ok(response);
        }

    }


    @PostMapping("main/api/post")
    public PersonReact postUser(@RequestBody @Valid PersonReact jsonString, BindingResult bindingResult) {

        System.out.println("Получили от фронта : " + jsonString.getLogin());
        System.out.println("Пришел пароль : " + jsonString.getPassword());

        personReactValidator.validate(jsonString, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("Нашли ошибки: " + bindingResult.getAllErrors());
            return null;
        }
        personReactRegistrationService.register(jsonString);
        return jsonString;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("main/api/postadmin/avatar")
    public ResponseEntity<?> postUserAvatarAdmin(@RequestParam("file") MultipartFile file, @RequestParam("login") String login) throws IOException {
        System.out.println("Логин пользователя: " + login);
        AddUserResponse response = new AddUserResponse();
            Optional<PersonReact> personReact = personReactRepository.findByLogin(login);
            if (personReact.isEmpty()){
                response.setMessage("Пользователь не определен системой");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            } else {
             if (file != null) {
                 File uploadDir = new File(uploadPath);
                 if (!uploadDir.exists()) {
                     uploadDir.mkdir();
                 }
                 String uuidFile = UUID.randomUUID().toString();
                 String resultFileName = uuidFile + "_" + file.getOriginalFilename();
                 file.transferTo(new File(uploadPath + "/" + resultFileName));
                 int id = personReact.get().getId();
                 System.out.println("ID у пользователя : " + id);
                 personReact.get().setFileName(resultFileName);
                 personReactRegistrationService.save(personReact.get());
                 //personReactRegistrationService.deleteById(id);

                 response.setMessage("Серевер сохранил аватар");
                 System.out.println("Cервер установил у " + personReact.get().getLogin() + " путь: " + personReact.get().getFileName());
                 return ResponseEntity.ok(response);
             } else {
                 response.setMessage("Неизвестная ошибка на стороне сервера");
                 return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
             }
            }
    }

    @PostMapping("/main/api/update")
    public ResponseEntity<?> postUserAdmin(@RequestBody @Valid PersonReact jsonString, BindingResult bindingResult){

        AddUserResponse response = new AddUserResponse();

        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println (error.getField() + " - " + error.getDefaultMessage());
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
        Optional<PersonReact> personReact = personReactRepository.findById(jsonString.getId());
        if (personReact.isEmpty()){
            response.setMessage("Пользователь не определен системой");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else
            personReact.get().setLogin(jsonString.getLogin());
            personReact.get().setEmail(jsonString.getEmail());
            personReact.get().setPhone(jsonString.getPhone());
            personReact.get().setPassword(jsonString.getPassword());
            personReact.get().setRole(jsonString.getRole());

            personReactRegistrationService.save(personReact.get());

            response.setMessage("Серевер сохранил пользователя");
            return ResponseEntity.ok(response);
    }
    @PostMapping("main/api/postadmin")
    public ResponseEntity<?> UpdateUserAdmin(@RequestBody @Valid PersonReact jsonString, BindingResult bindingResult){

        System.out.println("Получили от фронта : " + jsonString.getLogin());
        System.out.println("Пришел пароль : " + jsonString.getPassword());
        AddUserResponse response = new AddUserResponse();
        personReactValidator.validate(jsonString, bindingResult);
        if(bindingResult.hasErrors()){
            System.out.println("Нашли ошибки: " + bindingResult.getAllErrors());
            System.out.println(bindingResult.getFieldError());
            System.out.println(bindingResult.getFieldValue("password"));
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println (error.getField() + " - " + error.getDefaultMessage());
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
        personReactRegistrationService.register(jsonString);
        response.setMessage("Серевер сохранил пользователя");
        return ResponseEntity.ok(response);
    }
}
