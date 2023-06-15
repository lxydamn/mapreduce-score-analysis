package com.example.backend.controller;

import com.example.backend.service.MaxMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MaxController {

    @Autowired
    private MaxMapService maxMapService;

    @PostMapping("/api/max/map")
    public Map<String, String> max(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        return maxMapService.maxMap(files.split(","));
    }
}
