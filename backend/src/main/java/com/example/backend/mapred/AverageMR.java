package com.example.backend.mapred;

import com.example.backend.mapred.utils.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class AverageMR {
    static {
        try {
            System.load("C:\\hadoop-3.1.3\\bin\\hadoop.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }
    private final Configuration configuration;
    /**
     * 根据参数判断逆序正序
     */
    public AverageMR() {
        configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://47.115.231.140:9000");
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.set("dfs.client.use.datanode.hostname", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        configuration.setBoolean("dfs.client.block.write.replace-datanode-on-failure.enabled",true);
    }


    public void run (Boolean desc, String[] files, String output)
            throws IOException, InterruptedException, ClassNotFoundException {

        Job job = new Job(configuration, "Average");
        job.setJarByClass(AverageMR.class);
        job.setMapperClass(AverageMR.avgMapper.class);
        job.setReducerClass(AverageMR.avgReducer.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setMapOutputKeyClass(AverageOutputDesc.class);

        job.setOutputKeyClass(AverageOutputDesc.class);
        job.setOutputValueClass(NullWritable.class);

        job.setInputFormatClass(MyInputFormat.class);
        job.setGroupingComparatorClass(AverageGroupComparator.class);


        for (String file : files) {
            FileInputFormat.addInputPath(job, new Path("/inputs/" + file));
        }

        FileOutputFormat.setOutputPath(job, new Path("/" + output));

        job.waitForCompletion(true);

    }

    public static class avgMapper extends Mapper<Object, Text, AverageOutputDesc, DoubleWritable> {
        private final Text subj = new Text();
        private final DoubleWritable score = new DoubleWritable();
        @Override
        protected void map(Object key, Text value, Mapper<Object, Text, AverageOutputDesc, DoubleWritable>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\n");
            String[] headers = split[0].split("\\s+");

            for (int i = 1; i < split.length; i ++) {
                String line = split[i];
                String[] cells = line.split("\\s+");
                for (int j = 2; j < cells.length; j++) {
                    subj.set(headers[j]);
                    score.set(Double.parseDouble(cells[j]));
                    context.write(new AverageOutputDesc(subj, score), score);
                }
            }
        }
    }

    public static class avgReducer extends Reducer<AverageOutputDesc, DoubleWritable, AverageOutputDesc, NullWritable> {
        @Override
        protected void reduce(AverageOutputDesc key, Iterable<DoubleWritable> values, Reducer<AverageOutputDesc, DoubleWritable, AverageOutputDesc, NullWritable>.Context context) throws IOException, InterruptedException {
            int n = 0;
            double sum = 0;

            for (DoubleWritable value : values) {
                sum += value.get();
                n ++;
            }
            if(n != 0) sum /= n;
            key.setScore(new DoubleWritable(sum));
            context.write(key, NullWritable.get());
        }
    }

}
