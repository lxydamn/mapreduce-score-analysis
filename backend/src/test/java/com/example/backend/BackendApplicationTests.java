package com.example.backend;

import com.example.backend.utils.HDFSUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        HDFSUtils utils = new HDFSUtils();
        utils.deleteOrCreateFile("/test");
    }

}
