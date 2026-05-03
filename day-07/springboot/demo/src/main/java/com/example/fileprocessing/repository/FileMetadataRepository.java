package com.example.fileprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fileprocessing.entity.FileMetadata;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
}
