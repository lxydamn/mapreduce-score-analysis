package com.example.backend.controller;

import com.example.backend.service.MinMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MinController {

    @Autowired
    private MinMapService minMapService;

    @PostMapping("/api/min/map")
    public Map<String, String> min(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        return minMapService.minMap(files.split(","));
    }
}
