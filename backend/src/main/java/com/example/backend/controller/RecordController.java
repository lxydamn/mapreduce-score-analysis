package com.example.backend.controller;

import com.example.backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecordController {
    @Autowired
    private RecordService recordService;

    @GetMapping("/api/record/content")

    public String getRecord(@RequestParam("fileName") String fileName) {
        return recordService.getRecord(fileName);
    }
}
