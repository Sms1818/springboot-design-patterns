package com.example.fileprocessing.dto;

public class FileUploadResponse {
    private String message;
    private String fileType;
    private String status;

    public FileUploadResponse(String message, String fileType, String status) {
        this.message = message;
        this.fileType = fileType;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getFileType() {
        return fileType;
    }

    public String getStatus() {
        return status;
    }
}
