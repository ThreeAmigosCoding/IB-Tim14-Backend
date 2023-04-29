package com.example.demo.dto.certificate;

import org.springframework.core.io.Resource;

public class DownloadDto {

    private Resource downloadResource;
    private String contentType;

    public DownloadDto(Resource downloadResource, String contentType) {
        this.downloadResource = downloadResource;
        this.contentType = contentType;
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
}
