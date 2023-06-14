package com.example.backend.mapred.utils;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class WholeFileRecordReader extends RecordReader<Text, Text> {
    private FileSplit fileSplit;
    private JobContext jobContext;
    private Text currentKey = new Text();
    private Text currentValue = new Text();
    private boolean finishConverting = false;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.fileSplit = (FileSplit) inputSplit;
        this.jobContext = taskAttemptContext;
        String filename = fileSplit.getPath().getName();
        this.currentKey = new Text(filename);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {

        if (! finishConverting) {
            Path file = fileSplit.getPath();
            FileSystem fs = file.getFileSystem(jobContext.getConfiguration());
            FSDataInputStream in = fs.open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line="";
            StringBuilder total= new StringBuilder();
            while((line= br.readLine())!= null){
                total.append(line).append("\n");
            }
            br.close();
            in.close();
            fs.close();
            currentValue = new Text(total.toString());
            finishConverting = true;
            return true;
        }
        return false;
    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return currentKey;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return currentValue;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        float progress = 0;
        if(finishConverting){
            progress = 1;
        }
        return progress;
    }

    @Override
    public void close() throws IOException {

    }
}
