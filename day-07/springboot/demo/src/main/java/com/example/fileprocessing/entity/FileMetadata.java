package com.example.fileprocessing.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String uploadedBy;
    private String status;

    private LocalDateTime uploadTime;

    public FileMetadata() {
    }

    public FileMetadata(String fileName, String fileType, String uploadedBy, String status) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadedBy = uploadedBy;
        this.status = status;
        this.uploadTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

}
