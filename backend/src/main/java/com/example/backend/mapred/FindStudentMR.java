package com.example.backend.mapred;

import com.example.backend.mapred.utils.AverageGroupComparator;
import com.example.backend.mapred.utils.AverageOutputDesc;
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

public class FindStudentMR {

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
    public FindStudentMR() {
        configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://47.115.231.140:9000");
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.set("dfs.client.use.datanode.hostname", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        configuration.setBoolean("dfs.client.block.write.replace-datanode-on-failure.enabled",true);
    }


    public void run (String student, String[] files, String output)
            throws IOException, InterruptedException, ClassNotFoundException {

        configuration.set("student", student);
        Job job = new Job(configuration, "FindStudent");
        job.setJarByClass(FindStudentMR.class);
        job.setMapperClass(FindStudentMR.FindMapper.class);
        job.setReducerClass(FindStudentMR.FindReduce.class);
        job.setMapOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(MyInputFormat.class);

        for (String file : files) {
            FileInputFormat.addInputPath(job, new Path("/inputs/" + file));
        }

        FileOutputFormat.setOutputPath(job, new Path("/" + output));

        job.waitForCompletion(true);

    }

    public static class FindMapper extends Mapper<Object, Text, Text, Text> {
        private final Text name = new Text();
        private final Text courseAndScore = new Text();

        @Override
        protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\n");
            String[] headers = split[0].split("\\s+");

            for (int i = 1; i < split.length; i ++) {
                String line = split[i];
                String[] cells = line.split("\\s+");
                for (int j = 2; j < cells.length; j++) {
                    name.set(cells[1]);
                    courseAndScore.set(headers[j] + " : " + cells[j]);
                    context.write(name, courseAndScore);
                }
            }
        }
    }

    public static class FindReduce extends Reducer<Text, Text, Text, NullWritable> {
        private String student = "";
        private final Text output = new Text();

        @Override
        protected void setup(Reducer<Text, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            student = context.getConfiguration().get("student");

        }

        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            if (! student.equals(key.toString())) {
                return;
            }
            context.write(new Text("学生姓名：" + student), NullWritable.get());
            for (Text value : values) {
                output.set(value);
                context.write(output, NullWritable.get());
            }
        }
    }

}
