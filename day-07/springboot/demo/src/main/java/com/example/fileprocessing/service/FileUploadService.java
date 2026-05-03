package com.example.fileprocessing.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileprocessing.dto.FileUploadResponse;
import com.example.fileprocessing.entity.FileMetadata;
import com.example.fileprocessing.factory.FileProcessorFactory;
import com.example.fileprocessing.processor.FileProcessor;
import com.example.fileprocessing.repository.FileMetadataRepository;

@Service
public class FileUploadService {
    private final FileProcessorFactory fileProcessorFactory;
    private final FileMetadataRepository fileMetadataRepository;
    private final Optional<VirusScanService> virusScanService;

    public FileUploadService(FileProcessorFactory fileProcessorFactory, FileMetadataRepository fileMetadataRepository,
            Optional<VirusScanService> virusScanService) {
        this.fileProcessorFactory = fileProcessorFactory;
        this.fileMetadataRepository = fileMetadataRepository;
        this.virusScanService = virusScanService;
    }

    @Transactional
    public FileUploadResponse uploadAndProcessFile(MultipartFile file, String fileType, String uploadedBy) {
        FileMetadata metadata = new FileMetadata(
                file.getOriginalFilename(),
                fileType.toUpperCase(),
                uploadedBy,
                "PROCESSING");
        fileMetadataRepository.save(metadata);

        virusScanService.ifPresent(scanner -> scanner.scan(file));

        FileProcessor processor = fileProcessorFactory.getFileProcessor(fileType);
        processor.process(file);

        metadata.setStatus("SUCCESS");

        return new FileUploadResponse(
                "File uploaded and processed successfully",
                fileType.toUpperCase(),
                "SUCCESS");

    }
}