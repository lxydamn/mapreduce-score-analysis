package com.example.backend.controller;

import com.example.backend.service.SamePeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SamePeopleController {
    @Autowired
    SamePeopleService samePeopleService;

    @PostMapping("/api/samePeople/map")
    public Map<String, String> samePeople(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        System.out.println(files);
        return samePeopleService.samePeopleMap(files.split(","));
    }
}
