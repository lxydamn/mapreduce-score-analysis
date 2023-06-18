package com.example.backend.mapred;

import com.example.backend.mapred.utils.AverageGroupComparator;
import com.example.backend.mapred.utils.AverageOutputDesc;
import com.example.backend.mapred.utils.IntervalOutput;
import com.example.backend.mapred.utils.MyInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class StatisticIntervalMR {
    static {
        try {
            System.load("C:\\hadoop-3.1.3\\bin\\hadoop.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    private final Configuration configuration;


    public StatisticIntervalMR() {
        configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://47.115.231.140:9000");
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.set("dfs.client.use.datanode.hostname", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        configuration.setBoolean("dfs.client.block.write.replace-datanode-on-failure.enabled",true);
    }

    public void run (String[] files, String output)
            throws IOException, InterruptedException, ClassNotFoundException {

        Job job = new Job(configuration, "StatisticInterval");
        job.setJarByClass(StatisticIntervalMR.class);
        job.setMapperClass(StatisticIntervalMR.StaIntMapper.class);
        job.setReducerClass(StatisticIntervalMR.StaIntReducer.class);

        job.setMapOutputKeyClass(IntervalOutput.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(IntervalOutput.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(MyInputFormat.class);

        for (String file : files) {
            FileInputFormat.addInputPath(job, new Path("/inputs/" + file));
        }

        FileOutputFormat.setOutputPath(job, new Path("/" + output));

        job.waitForCompletion(true);

    }

    public static class StaIntMapper extends Mapper<Object, Text, IntervalOutput, IntWritable> {
        private Text course = new Text();
        private final static IntWritable one = new IntWritable(1);
        @Override
        protected void map(Object key, Text value, Mapper<Object, Text, IntervalOutput, IntWritable>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\n");
            String[] headers = split[0].split("\\s+");

            for (int i = 1; i < split.length; i ++) {
                String line = split[i];
                String[] cells = line.split("\\s+");
                for (int j = 2; j < cells.length; j++) {
                    course.set(headers[j]);
                    Double score = Double.parseDouble(cells[j]);
                    IntWritable type = new IntWritable();
                    if(score < 60.0) {
                        type.set(1);
                    } else if (score >= 60.0 && score < 70.0) {
                        type.set(2);
                    } else if (score >= 70.0 && score < 80.0) {
                        type.set(3);
                    } else if (score >= 80.0 && score < 90.0) {
                        type.set(4);
                    } else {
                        type.set(5);
                    }
                    context.write(new IntervalOutput(course,type), one);
                }
            }
        }
    }

    public static class StaIntReducer extends Reducer<IntervalOutput, IntWritable, IntervalOutput, IntWritable> {
        private IntWritable result = new IntWritable();
        @Override
        protected void reduce(IntervalOutput key, Iterable<IntWritable> values, Reducer<IntervalOutput, IntWritable, IntervalOutput, IntWritable>.Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
}
