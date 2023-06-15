package com.example.backend.service.impl;

import com.example.backend.mapred.CountPeopleMR;
import com.example.backend.mapred.StatisticIntervalMR;
import com.example.backend.service.StatisticIntervalService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StatisticIntervalServiceImpl implements StatisticIntervalService {
    @Override
    public Map<String, String> statisticIntervalMap(String[] files) {
        Map<String, String> resp = new HashMap<>();

        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;

        StatisticIntervalMR statisticIntervalMR = new StatisticIntervalMR();

        try {
            statisticIntervalMR.run(files, resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "interval");

        } catch (Exception e) {
            resp.put("error_info", "计算失败");
            return resp;
        }
        resp.put("error_info", "success");
        resp.put("filePath", resultName + "-" + "interval");
        return resp;
    }
}
