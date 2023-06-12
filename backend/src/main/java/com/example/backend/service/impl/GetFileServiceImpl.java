package com.example.backend.service.impl;

import com.example.backend.pojo.GFile;
import com.example.backend.service.GetFileService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GetFileServiceImpl implements GetFileService {
    @Override
    public List<GFile> getInputFiles() {
        try {
            HDFSUtils utils = new HDFSUtils();
            return utils.getInputFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GFile> getRecordFiles() {
        try {
            HDFSUtils utils = new HDFSUtils();
            return utils.getRecordFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
