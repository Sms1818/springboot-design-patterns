package com.example.fileprocessing.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "virus.scan.enabled", havingValue = "true")
public class BasicVirusScanService implements VirusScanService {
    @Override
    public void scan(MultipartFile file) {
        // Implement basic virus scanning logic here
        System.out.println("Scanning file for viruses: " + file.getOriginalFilename());
    }

}
