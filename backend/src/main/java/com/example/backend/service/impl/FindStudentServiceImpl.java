package com.example.backend.service.impl;

import com.example.backend.mapred.CountPeopleMR;
import com.example.backend.mapred.FindStudentMR;
import com.example.backend.service.FindStudentService;
import com.example.backend.utils.HDFSUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FindStudentServiceImpl implements FindStudentService {
    @Override
    public Map<String, String> findStudent(String student, String[] files) {
        Map<String, String> resp = new HashMap<>();

        String resultName = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);;

        FindStudentMR findStudentMR = new FindStudentMR();

        try {
            findStudentMR.run(student,files,resultName);

            HDFSUtils utils = new HDFSUtils();

            utils.moveToRecord(resultName, null, "findStudent");

        } catch (Exception e) {
            resp.put("error_info", "查找失败");
            return resp;
        }
        resp.put("error_info", "success");
        resp.put("filePath", resultName + "-" + "findStudent");
        return resp;
    }
}
