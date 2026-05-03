package com.example.fileprocessing.processor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageFileProcessor implements FileProcessor {
    @Override
    public void process(MultipartFile file) {
        // Implement image file processing logic here
        System.out.println("Processing image file: " + file.getOriginalFilename());

    }

    @Override
    public String getFileType() {
        return "image";
    }

}
