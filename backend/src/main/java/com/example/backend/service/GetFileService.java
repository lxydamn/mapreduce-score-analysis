package com.example.backend.service;

import com.example.backend.pojo.GFile;
import org.apache.hadoop.fs.Path;

import java.util.List;

public interface GetFileService {
    List<GFile> getInputFiles();

    List<GFile> getRecordFiles();
}
