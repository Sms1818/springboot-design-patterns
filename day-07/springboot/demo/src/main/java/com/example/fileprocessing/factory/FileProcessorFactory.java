package com.example.fileprocessing.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.fileprocessing.exception.InvalidFileTypeException;
import com.example.fileprocessing.processor.FileProcessor;

@Component
public class FileProcessorFactory {
    private final Map<String, FileProcessor> fileProcessors = new HashMap<>();

    public FileProcessorFactory(List<FileProcessor> fileProcessors) {
        for (FileProcessor processor : fileProcessors) {
            this.fileProcessors.put(processor.getFileType(), processor);
        }
    }

    public FileProcessor getFileProcessor(String fileType) {
        FileProcessor fileProcessor = fileProcessors.get(fileType);
        if (fileProcessor == null) {
            throw new InvalidFileTypeException("Unsupported file type: " + fileType);
        }
        return fileProcessor;
    }

}
