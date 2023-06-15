package com.example.backend.controller;

import com.example.backend.service.CountPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CountPeopleController {

    @Autowired
    private CountPeopleService countPeopleService;

    @PostMapping("/api/countPeople/map")
    public Map<String, String> countPeople(@RequestParam Map<String, String> map) {
        String files = map.get("files");
        return countPeopleService.countPeopleMap(files.split(","));
    }
}
