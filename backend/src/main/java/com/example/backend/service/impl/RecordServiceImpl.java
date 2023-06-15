package com.example.backend.service.impl;

import com.example.backend.service.RecordService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RecordServiceImpl implements RecordService {

    @Override
    public String getRecord(String fileName) {
        try {
            HDFSUtils utils = new HDFSUtils();
            return utils.readContent(fileName);
        } catch (IOException e) {
            return null;
        }
    }
}
