package com.example.backend.controller;

import com.example.backend.service.StatisticIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatisticIntervalController {

    @Autowired
    private StatisticIntervalService statisticIntervalService;

    @PostMapping("/api/statisticInterval/map")
    public Map<String, String> statisticInterval(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        return statisticIntervalService.statisticIntervalMap(files.split(","));
    }
}
