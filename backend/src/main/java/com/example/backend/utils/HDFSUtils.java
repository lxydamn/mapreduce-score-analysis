package com.example.backend.utils;

import com.example.backend.pojo.GFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.kerby.util.IOUtil;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class HDFSUtils{

    // 文件系统配置信息
    private final Configuration configuration;
    // 文件系统操作
    private final FileSystem fileSystem;
    // 基本文件路径
    private static final String BASE_LINK = "hdfs://47.115.231.140:9000/";
    // 输入文件基本路径
    private static final String BASE_URL = "hdfs://47.115.231.140:9000/inputs/";
    // 成绩分析结果文件基本路径
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
            System.out.println(content);
            fsDataOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
    }



    /**
     * 防止文件重名
     * 循环在文件名后添加1,2,3...n
     * 并将创建好后的文件名返回
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
    public List<GFile> getInputFiles() throws IOException {

        return getGFiles(BASE_URL);
    }

    /**
     * 读出记录文件列表
     */
    public List<GFile> getRecordFiles() throws IOException {
        return getGFiles(RECORD_BASE_URL);
    }

    private List<GFile> getGFiles(String recordBaseUrl) throws IOException {
        List<GFile> list = new ArrayList<>();

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(recordBaseUrl));

        /**
         * 将所有的文件全部转化为GFile文件类型
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (FileStatus fileStatus : fileStatuses) {
            list.add(new GFile(
                    fileStatus.getPath().getName(),
                    fileStatus.getLen(),
                    simpleDateFormat.format(new Date(fileStatus.getModificationTime()))
            ));
        }
        return list;
    }

    /**
     * 读取文件内容
     */
    public String readContent(String file) throws IOException {
        Path path = new Path(RECORD_BASE_URL + file);

        if (!fileSystem.exists(path)) {
            return null;
        }
        FSDataInputStream open = fileSystem.open(path);

        return IOUtil.readInput(open);
    }

    /**
     * 移动 output 的内容到  record
     */
    public void moveToRecord(String fileName, String reName, String type) throws IOException {
        Path path = new Path(BASE_LINK + fileName + "/part-r-00000");
        Path objPath = new Path(RECORD_BASE_URL + fileName + "-" + type);
        if (!fileSystem.exists(path)) {
            return;
        }
        fileSystem.rename(path, objPath);
        fileSystem.delete(new Path(BASE_LINK + fileName), true);
    }


}

