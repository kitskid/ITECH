package com.example.securityapplication.services;

import com.example.securityapplication.models.Attachment;
import com.example.securityapplication.models.PersonReact;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.IOException;
import java.net.MalformedURLException;

public interface AttachmentService {
    Attachment addAttachment(MultipartFile file, String login) throws IOException;
    Attachment findAttachById(Long attachId) throws Exception;
    Resource loadFileAsResource(String fileName) throws MalformedURLException;
}
