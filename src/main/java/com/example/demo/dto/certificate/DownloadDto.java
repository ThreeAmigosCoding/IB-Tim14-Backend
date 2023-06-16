package com.example.demo.dto.certificate;

import org.springframework.core.io.Resource;

public class DownloadDto {

    private Resource downloadResource;
    private String contentType;
    private String fileName;

    public DownloadDto(Resource downloadResource, String contentType, String fileName) {
        this.downloadResource = downloadResource;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public DownloadDto() {
    }

    public Resource getDownloadResource() {
        return downloadResource;
    }

    public void setDownloadResource(Resource downloadResource) {
        this.downloadResource = downloadResource;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
