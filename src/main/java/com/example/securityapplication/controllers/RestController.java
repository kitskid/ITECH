package com.example.securityapplication.controllers;

import com.example.securityapplication.models.UserJs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class RestController {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/restapi")
    public String requestMethod() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(restTemplate.getForObject("https://reqres.in/api/users?page=2", String.class));
        String response = restTemplate.getForObject("https://reqres.in/api/users?page=2", String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map<String, Object>  userJsMap = mapper.readValue(response, new TypeReference<>(){});
        List<UserJs> userJsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry: userJsMap.entrySet()){
            //System.out.println("Поле первого уровня: " + entry.getKey());
            if (entry.getKey().equals("data")){
                List<LinkedHashMap<String, Object>> response2 = (ArrayList) entry.getValue();
                for (LinkedHashMap<String, Object> users: response2){
                    String email = null;
                    String first_name = null;
                    String last_name = null;
                    String avatar = null;

                    for(Map.Entry<String, Object> user: users.entrySet()){
                        if(user.getKey().equals("id")){
                           int id = (Integer)user.getValue();
                           // System.out.println("Название поля юзера : " + user.getKey() + " Значение этого поля : " + id);
                        }  else if(user.getKey().equals("email")){
                            email = (String) user.getValue();
                        } else if(user.getKey().equals("first_name")) {
                            first_name = (String) user.getValue();
                        } else if(user.getKey().equals("last_name")){
                            last_name = (String) user.getValue();
                        } else if(user.getKey().equals("avatar")) {
                            avatar = (String) user.getValue();
                        }
                        //System.out.println("Название поля юзера : " + user.getKey() + " Значение этого поля : " + (String) user.getValue());
                    }
                    userJsList.add(new UserJs(email, first_name, last_name, avatar));
                    //System.out.println("===========================================================");
                }

            }
        }
        for (UserJs userJs: userJsList){
            System.out.println("Пользователь: " + userJs.getFirst_name() + " " + userJs.getLast_name());
        }

        //JsonNode obj = mapper.readTree(response);


        Map<String, String> js = new HashMap<>();
        js.put("name", "Антон");
        js.put("job", "IBM");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(js);
        //System.out.println(restTemplate.postForObject("https://reqres.in/api/users", request, String.class));
        return "admin/admin";
    }

    @ResponseBody
    @GetMapping("/api/info")
    public String getInfo(){
        return "Данное апи для вывода информации о пользователях системы";
    }



}
