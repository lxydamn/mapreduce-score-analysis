package com.example.backend;

import com.example.backend.utils.HDFSUtils;
import com.example.backend.utils.WordCount;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {

        HDFSUtils utils = new HDFSUtils();
//        try {
//            utils.inputContent("Dear Mr. Smith ,\n" +
//                    "\n" +
//                    "I am Lihua ,chairman of the student union , from chenguang high schoo. I am very pleased to learn that you are coming to visit our school on June 26. I am writing to tell you what we have arranged for you.\n" +
//                    "\n" +
//                    "In the morning , there will be a forum in the school auditorium , where visitors and students from our school communicate with each other ,talking about school life and cultural differences . At noon, you are invited to have lunch in our school cafeteria with students from our school. You can taste dumplings ,noodles and other Chinese foods .In the afternoon, the students in our school will show you around the HaiHe river .\n" +
//                    "\n" +
//                    "How do you like the arrangements ? I hope you will have a nice time in Tian jing\n" +
//                    "\n" +
//                    "Yours sincerely ,\n" +
//                    "\n" +
//                    "Li Hua", "/test.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
        try {
            utils.printContent("/output/part-r-00000");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        WordCount wordCount = new WordCount();
//        try {
//            utils.deleteFile("/output");
//            wordCount.run("/test.txt", "/output");
//        } catch (IOException | InterruptedException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

}
