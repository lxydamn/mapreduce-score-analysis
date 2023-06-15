package com.example.backend.service.impl;

import com.example.backend.mapred.MaxMR;
import com.example.backend.service.MaxMapService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MaxMapServiceImpl implements MaxMapService {
    @Override
    public Map<String, String> maxMap(String[] files) {
        Map<String, String> resp = new HashMap<>();

        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;

        MaxMR maxMR = new MaxMR();

        try {
            maxMR.run(files, resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "max");

        } catch (Exception e) {
            resp.put("error_info", "计算失败");
            return resp;
        }
        resp.put("error_info", "success");
        resp.put("filePath", resultName + "-" + "max");
        return resp;
    }
}
