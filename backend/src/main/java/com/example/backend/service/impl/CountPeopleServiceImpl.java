package com.example.backend.service.impl;

import com.example.backend.mapred.CountPeopleMR;
import com.example.backend.service.CountPeopleService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CountPeopleServiceImpl implements CountPeopleService {
    @Override
    public Map<String, String> countPeopleMap(String[] files) {
        Map<String, String> resp = new HashMap<>();

        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;

        CountPeopleMR countPeopleMR = new CountPeopleMR();

        try {
            countPeopleMR.run(files, resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "countPeople");

        } catch (Exception e) {
            resp.put("error_info", "计算失败");
            return resp;
        }
        resp.put("error_info", "success");
        resp.put("filePath", resultName + "-" + "countPeople");
        return resp;
    }
}
