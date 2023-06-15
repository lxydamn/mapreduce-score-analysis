package com.example.backend.controller;

import com.example.backend.service.FindStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FindStudentController {

    @Autowired
    private FindStudentService findStudentService;

    @PostMapping("/api/findStudent/map")
    public Map<String, String> findStudent(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        String student = map.get("student");
        return findStudentService.findStudent(student,files.split(","));
    }
}
