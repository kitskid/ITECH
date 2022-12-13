package com.example.securityapplication.services;

import com.example.securityapplication.models.Attachment;
import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class AttachmentServiceImpl implements AttachmentService{

    private final AttachmentRepository attachmentRepository;


    /*
     * Загрузить новый файл
     *
     * @param file
     * @param user
     * @throws IOException
     */

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;

    }

    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public Attachment addAttachment(MultipartFile file, String login) throws IOException {
        // Создаем директорию если ее не существует
        File uploadDir = new File(uploadPath);
        // Если директория uploads не существует, то создаем ее
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String curDate = LocalDateTime.now().toString();
        // Создаем уникальное название для файла и загружаем файл
        String fileName =
                "attach_" + curDate + "_" + file.getOriginalFilename().toLowerCase().replaceAll(" ", "-");

        file.transferTo(new File(uploadDir + "/" + fileName));
        Attachment attachment = new Attachment();
        attachment.setAutor(login);
        attachment.setAttachTitle(fileName);
        attachment.setDownloadLink("/attachments/get/" + fileName);
        attachment.setExtension(getFileExtension(file));
        attachment.setUploadDate(LocalDate.now());


 //       Attachment attachment = Attachment.builder()
 //               .attachTitle(fileName)
 //               .uploadDate(LocalDate.now())
 //               .extension(fileTools.getFileExtension(file.getOriginalFilename()))
 //               .downloadLink("/attachments/get/" + fileName)
 //               .build();
        attachmentRepository.save(attachment);
        return attachment;
    }
    /*
     * Найти Вложение по его ID
     *
     * @param attachId
     * @return
     */

    @Override
    public Attachment findAttachById(Long attachId) throws Exception {
        return attachmentRepository
                .findById(attachId)
                .orElseThrow(() -> new Exception("Attachment not found!"));
    }
    /*
     * Скачать файл
     *
     * @param fileName
     * @return
     * @throws MalformedURLException
     */
    @Override
    public Resource loadFileAsResource(String fileName)
            throws MalformedURLException {
        Path fileStorageLocation =
                Paths.get(uploadPath).toAbsolutePath().normalize();
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        UrlResource urlResource = new UrlResource(filePath.toUri());
        return urlResource;
    }

    public String getFileExtension(MultipartFile file) {
        String[] filenameParts = file.getOriginalFilename().split("(-|\\.)");

        if(filenameParts.length > 1) {
            return filenameParts[filenameParts.length - 1];
        }
        else {
            return null;
        }
    }
}
