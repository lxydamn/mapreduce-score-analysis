package com.example.backend.controller;

import com.example.backend.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class UploadFileController {

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping("/api/upload")
    public Map<String, String> upload(@RequestParam(value = "files") MultipartFile[] files) {
        return uploadFileService.upload(files);
    }
}
