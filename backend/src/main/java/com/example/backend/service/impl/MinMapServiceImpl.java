package com.example.backend.service.impl;

import com.example.backend.mapred.MinMR;
import com.example.backend.service.MinMapService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MinMapServiceImpl implements MinMapService {
    @Override
    public Map<String, String> minMap(String[] files) {
        Map<String, String> resp = new HashMap<>();

        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;

        MinMR minMR = new MinMR();

        try {
            minMR.run(files, resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "min");

        } catch (Exception e) {
            resp.put("error_info", "计算失败");
            return resp;
        }
        resp.put("error_info", "success");
        resp.put("filePath", resultName + "-" + "min");
        return resp;
    }
}
