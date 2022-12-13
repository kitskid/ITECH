package com.example.securityapplication.controllers;

import com.example.securityapplication.models.Attachment;
import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.PersonReactRepository;
import com.example.securityapplication.services.AttachmentService;
import com.example.securityapplication.services.PersonReactDetailsService;
import com.example.securityapplication.services.PersonReactRegistrationService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE})
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final PersonReactDetailsService personReactDetailsService;
    private final PersonReactRepository personReactRepository;
    private final PersonReactRegistrationService personReactRegistrationService;

    @Value("${upload.path}")
    private String uploadPath;

    public AttachmentController(AttachmentService attachmentService, PersonReactDetailsService personReactDetailsService, PersonReactRepository personReactRepository, PersonReactRegistrationService personReactRegistrationService) {
        this.attachmentService = attachmentService;
        this.personReactDetailsService = personReactDetailsService;
        this.personReactRepository = personReactRepository;
        this.personReactRegistrationService = personReactRegistrationService;
    }

    /*
     * Загрузить новое вложение
     *
     * @param file
     * @return
     * @throws IOException
     */

    @PostMapping(value = "/add", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadAttachment(
            @RequestPart(value = "file") MultipartFile file, @RequestParam("login") String login)
            throws IOException {



        System.out.println("Логин пользователя: " + login);
        Optional<PersonReact> personReact = personReactRepository.findByLogin(login);


        if (personReact.isEmpty()){
            Map<String, String> attachmentStatus = new HashMap<>();
            attachmentStatus.put("status", "ok");
            attachmentStatus.put("message", "пользователь не найден, данные не сохранены");
            return ResponseEntity.ok(attachmentStatus);
        } else {

            Attachment attachment = attachmentService.addAttachment(file, login);
            personReact.get().setFileName(attachment.getAttachTitle());
            personReactRegistrationService.save(personReact.get());

            Map<String, String> attachmentStatus = new HashMap<>();
            attachmentStatus.put("status", "ok");
            attachmentStatus.put("attachId", attachment.getAttachId().toString());
            attachmentStatus.put("login", attachment.getAutor());

            return ResponseEntity.ok(attachmentStatus);
        }
    }

    /*
     * Получить ссылку на скачивание загруженного файла
     *
     * @param filename
     * @param request
     * @return
     * @throws IOException
     */

    //@GetMapping("/get/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) throws IOException {

        Resource resource = attachmentService.loadFileAsResource(filename);
        String contentType;
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping(value = "main/api/postadmin/avatar/get/{filename:.+}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageJpg(@PathVariable String filename) throws IOException {

        boolean isFileExist = false;
        Path pathFile = Paths.get(uploadPath+ "/avatar.png");
        if(Files.exists(pathFile)) isFileExist = true;

        if(filename.isEmpty() && !isFileExist) {
            return null;
        }
        if(filename.isEmpty() && isFileExist){
            InputStream infile = new FileInputStream(pathFile.toString());
            return IOUtils.toByteArray(infile);
        }
        if(!filename.isEmpty()) {
            Path path = Paths.get(uploadPath+ "/" + filename);
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

