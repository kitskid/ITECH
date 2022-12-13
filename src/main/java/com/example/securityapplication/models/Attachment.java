package com.example.securityapplication.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long attachId;

    @Column(name = "attachname")
    private String attachTitle;

    @Column(nullable = false, updatable = false)
    private LocalDate uploadDate;

    @Column(name = "extention")
    private String extension;

    @Column(name = "link")
    private String downloadLink;

    @Column(name = "autor")
    private String autor;

    private String message;

    public Attachment() {
    }

    public Long getAttachId() {
        return attachId;
    }

    public String getAttachTitle() {
        return attachTitle;
    }

    public void setAttachTitle(String attachTitle) {
        this.attachTitle = attachTitle;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
