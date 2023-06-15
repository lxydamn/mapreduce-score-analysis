package com.example.backend.service;

import java.util.Map;

public interface FindStudentService {
    Map<String, String> findStudent(String student, String[] files);
}
