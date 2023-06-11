package com.example.backend.service.impl;

import com.example.backend.service.UploadFileService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Override
    public Map<String, String> upload(MultipartFile[] files) {
        Map<String, String> resp = new HashMap<>();
        try {
            HDFSUtils utils = new HDFSUtils();
            utils.uploadFiles(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
