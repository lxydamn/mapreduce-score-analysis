package com.example.backend;

import com.example.backend.mapred.AverageMR;
import com.example.backend.utils.HDFSUtils;
import com.example.backend.utils.WordCount;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() throws IOException {
//        AverageMR averageMR = new AverageMR();
//        String[] files = new String[] {"test.txt", "test1.txt"};
//        try {
//            averageMR.run(false, files);
//
//        } catch (IOException | InterruptedException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        HDFSUtils utils = new HDFSUtils();
        System.out.println(utils.readContent("part-r-00000"));

    }

}
