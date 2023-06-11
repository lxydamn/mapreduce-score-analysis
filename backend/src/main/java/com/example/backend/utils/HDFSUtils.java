package com.example.backend.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HDFSUtils{

    private final Configuration configuration;

    private final FileSystem fileSystem;

    private static final String BASE_URL = "hdfs://47.115.231.140:9000/inputs/";

    private static final String RECORD_BASE_URL = "hdfs://47.115.231.140:9000/records/";

    public HDFSUtils() throws IOException {
        configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://47.115.231.140:9000");
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.set("dfs.client.use.datanode.hostname", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        configuration.setBoolean("dfs.client.block.write.replace-datanode-on-failure.enabled", true);

        fileSystem = FileSystem.get(configuration);
    }

    public void deleteFile(String file) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        if (fileSystem.exists(new Path(file))) {
            fileSystem.delete(new Path(file), true);
        } else {
            System.out.println("删除文件不存在");
        }

    }

    /**
     * 写入内容到文件中
     */
    public void inputContent(String content, String path) throws IOException {

            Path pathHdfs = new Path(path);

            if (!fileSystem.exists(pathHdfs)) {
                return;
            }

            FSDataOutputStream fsDataOutputStream = fileSystem.append(pathHdfs);

            fsDataOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 防止文件重名
     */
    private String createFile(String path) throws IOException {

        int i = 0;
        String[] split = path.split("\\.");

        if (!fileSystem.exists(new Path(BASE_URL + path))) {
            fileSystem.createNewFile(new Path(BASE_URL + path));
            return BASE_URL + path;
        }

        while (fileSystem.exists(new Path(BASE_URL + split[0] + "_" + i + "." + split[1]))) {
            i ++;
        }

        boolean ok = fileSystem.createNewFile(new Path(BASE_URL + split[0] + "_" + i + "." + split[1]));
        return BASE_URL + split[0] + "_" + i + "." + split[1];
    }

    /**
     * 上传文件至hdfs系统
     * @param files 文件
     * @throws IOException 异常
     */
    public void uploadFiles(MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {
            String name = createFile(Objects.requireNonNull(file.getOriginalFilename()));
            inputContent(new String(file.getBytes(), StandardCharsets.UTF_8), name);
        }

    }

    /**
     * 读出成绩文件列表
     */
    public List<String> getInputFiles() throws IOException {

        List<String> list = new ArrayList<>();

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(BASE_URL));

        for (FileStatus fileStatus : fileStatuses) {
            list.add(fileStatus.getPath().getName());
        }

        return list;
    }

    /**
     * 读出记录文件列表
     */
    public List<String> getRecordFiles() throws IOException {
        List<String> list = new ArrayList<>();

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(RECORD_BASE_URL));

        for (FileStatus fileStatus : fileStatuses) {
            list.add(fileStatus.getPath().getName());
        }

        return list;
    }



}

