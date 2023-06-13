package com.example.backend.service;

import java.util.Map;

public interface AverageMapService {
    Map<String, String> averageMap(String[] files, boolean isDesc);
}
