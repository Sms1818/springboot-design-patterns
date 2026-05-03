package com.example.fileprocessing.processor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CsvFileProcessor implements FileProcessor {
    @Override
    public void process(MultipartFile file) {
        // Implement CSV file processing logic here
        System.out.println("Processing CSV file: " + file.getOriginalFilename());

    }

    @Override
    public String getFileType() {
        return "csv";
    }

}
