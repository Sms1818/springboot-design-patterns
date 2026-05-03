package com.example.fileprocessing.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileprocessing.dto.FileUploadResponse;
import com.example.fileprocessing.service.FileUploadService;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public FileUploadResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType,
            @RequestParam("uploadedBy") String uploadedBy) {
        return fileUploadService.uploadAndProcessFile(file, fileType, uploadedBy);
    }

}
