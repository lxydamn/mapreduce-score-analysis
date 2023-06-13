package com.example.backend.controller;

import com.example.backend.service.AverageMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AverageController {

    @Autowired
    private AverageMapService averageMapService;

    @PostMapping("/api/average/map")
    public Map<String, String> average(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        String desc = map.get("desc");
        boolean isDesc = false;
        if (desc.equals("true")) {
            isDesc = true;
        }
        return averageMapService.averageMap(files.split(","), isDesc);
    }
}
