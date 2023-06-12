package com.example.backend.controller;

import com.example.backend.pojo.GFile;
import com.example.backend.service.GetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFileController {
    @Autowired
    private GetFileService getFileService;

    @GetMapping("/api/get/input")
    public List<GFile> getInput() {
        return getFileService.getInputFiles();
    }

    @GetMapping("/api/get/record")
    public List<GFile> getRecord() {
        return getFileService.getRecordFiles();
    }




}
