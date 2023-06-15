package com.example.backend;

import com.example.backend.mapred.AverageMR;
import com.example.backend.mapred.MaxMR;
import com.example.backend.mapred.MinMR;
import com.example.backend.mapred.StatisticIntervalMR;
import com.example.backend.utils.HDFSUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() throws IOException {
//        MinMR minMR = new MinMR();
        //FindStudentMR findStudentMR = new FindStudentMR();
        AverageMR averageMR = new AverageMR();
        String[] files = new String[] {"test.txt", "test1.txt"};
        try {
            String resultName = UUID
                    .randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8);;
            //findStudentMR.run("Rose", files, resultName);
            averageMR.run(true, files, resultName);
//            minMR.run(files, resultName);

            HDFSUtils utils = new HDFSUtils();
            utils.moveToRecord(resultName,"11", "avg");
            System.out.println(utils.readContent(resultName + "-" + "avg"));
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
