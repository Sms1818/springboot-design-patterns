package com.example.fileprocessing.processor;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessor {
    void process(MultipartFile file);

    String getFileType();
}
