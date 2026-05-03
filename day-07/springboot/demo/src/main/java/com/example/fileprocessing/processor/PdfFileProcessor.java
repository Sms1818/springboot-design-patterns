package com.example.fileprocessing.processor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfFileProcessor implements FileProcessor {
    @Override
    public void process(MultipartFile file) {
        // Implement PDF file processing logic here
        System.out.println("Processing PDF file: " + file.getOriginalFilename());

    }

    @Override
    public String getFileType() {
        return "pdf";
    }

}
