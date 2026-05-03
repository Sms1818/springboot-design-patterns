package com.example.fileprocessing.service;

import org.springframework.web.multipart.MultipartFile;

public interface VirusScanService {
    void scan(MultipartFile file);

}
