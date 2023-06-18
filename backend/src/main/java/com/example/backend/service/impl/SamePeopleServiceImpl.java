package com.example.backend.service.impl;

import com.example.backend.mapred.SamePeopleMR;
import com.example.backend.mapred.StatisticIntervalMR;
import com.example.backend.service.SamePeopleService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SamePeopleServiceImpl implements SamePeopleService {
    @Override
    public Map<String, String> samePeopleMap(String[] files) {
        Map<String, String> resp = new HashMap<>();
        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;
        SamePeopleMR samePeopleMR = new SamePeopleMR();
        try {
            samePeopleMR.run(files, resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "samePeople");
        } catch (Exception e) {
            resp.put("error_info", "计算失败");
            return resp;
        }
        resp.put("error_info", "success");
        resp.put("filePath", resultName + "-" + "samePeople");
        return resp;
    }
}
