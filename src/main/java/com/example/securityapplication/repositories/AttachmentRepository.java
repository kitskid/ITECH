package com.example.securityapplication.repositories;

import com.example.securityapplication.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
