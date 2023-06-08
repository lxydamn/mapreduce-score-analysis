package com.example.backend.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.kerby.util.IOUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;



public class HDFSUtils{

    private final Configuration configuration;
    public HDFSUtils() {
        configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://47.115.231.140:9000");
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.set("dfs.client.use.datanode.hostname", "true");
//        configuration.set("dfs.datanode.use.datanode.hostname", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        configuration.setBoolean("dfs.client.block.write.replace-datanode-on-failure.enabled", true);
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
     * 给定路径判断文件是否存在
     */
    public boolean fileIsExist(String fileName) {

        try {

            FileSystem fs = FileSystem.get(configuration);

            if(fs.exists(new Path(fileName))) {
                System.out.println("exist");
                fs.close();
                return true;
            } else {
                System.out.println("no exist");
                fs.close();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 传入本地文件路径
     * 上传到HDFS系统中的根路径
     */
    public void pushLocalFileToHDFS(String localPath) {

        try {
            FileSystem fileSystem = FileSystem.get(configuration);
            fileSystem.copyFromLocalFile(new Path(localPath), new Path("/"));
            fileSystem.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
     * 将HDFS文件中内容打印到终端中
     */
    public void printContent(String filePath) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);

        if (!fileSystem.exists(new Path(filePath))) {
            System.out.println("文件不存在");
            fileSystem.close();
            return;
        }

        FSDataInputStream fsDataInputStream = fileSystem.open(new Path(filePath));

        String content = IOUtil.readInput(fsDataInputStream);

        System.out.println(content);




    }

    public void inputContent(String content, String path) throws IOException {

            FileSystem fileSystem = FileSystem.get(configuration);
            Path pathHdfs = new Path(path);
            if (!fileSystem.exists(pathHdfs)) {
                fileSystem.createNewFile(pathHdfs);
                System.out.println("创建了文件");
            }

            FSDataOutputStream fsDataOutputStream = fileSystem.append(pathHdfs);

            fsDataOutputStream.write(content.getBytes(StandardCharsets.UTF_8));

            System.out.println("写入成功");

            fileSystem.close();
    }

    /**
     * 列出文件的权限等信息
     */
    public void listInfoOfFile(String filePath) {
        try {

            FileSystem fileSystem = FileSystem.get(configuration);

            Path path = new Path(filePath);

            FileStatus fileStatus = fileSystem.getFileStatus(path);

            printFileStatus(fileStatus);

            fileSystem.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printFileStatus(FileStatus fileStatus) {
        System.out.println("文件路径：" + fileStatus.getPath());
        System.out.println("读写权限：" + fileStatus.getPermission());
        System.out.println("文件大小：" + fileStatus.getLen());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("修改时间：" + formatter.format(new Date(fileStatus.getModificationTime())));
    }

    public void listInfoOfDir(String dirPath) {
        try {
            FileSystem fileSystem = FileSystem.get(configuration);

            Path path = new Path(dirPath);

            FileStatus[] fileStatuses = fileSystem.listStatus(path);

            for(FileStatus fileStatus : fileStatuses) {
                printFileStatus(fileStatus);
                if(fileStatus.isDirectory()) {
                    System.out.println("children：");
                    System.out.println("==========================");
                    listInfoOfDir(fileStatus.getPath().toString());
                    System.out.println("==========================");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void deleteOrCreateFile(String filePath) {
        try {
            FileSystem fileSystem = FileSystem.get(configuration);
            Scanner scanner = new Scanner(System.in);
            Path path = new Path(filePath);

            if (fileSystem.exists(path)) {
                System.out.println("文件存在！");

            } else {
                fileSystem.createNewFile(path);
                System.out.println("创建成功！");
            }

            fileSystem.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * filepath -> toPath 移动
     */
    public void removeFile(String filePath, String toPath) {

        try {
            FileSystem fileSystem = FileSystem.get(configuration);

            FileUtil.copy(fileSystem, new Path(filePath), fileSystem, new Path(toPath),true, true, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

