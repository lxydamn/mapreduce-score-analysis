package com.example.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadFileService {
    Map<String, String> upload(MultipartFile[] files);
}
