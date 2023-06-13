package com.example.backend.service.impl;

import com.example.backend.mapred.AverageMR;
import com.example.backend.service.AverageMapService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AverageMapServiceImpl implements AverageMapService {
    @Override
    public Map<String, String> averageMap(String[] files, boolean isDesc) {
        Map<String, String> resp = new HashMap<>();

        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;

        AverageMR averageMR = new AverageMR();

        try {
            averageMR.run(isDesc, files, resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "average");

        } catch (Exception e) {
            resp.put("error_info", "计算失败");
            return resp;
        }
        return resp;
    }
}
