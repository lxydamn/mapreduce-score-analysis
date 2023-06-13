package com.example.backend.mapred.utils;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AverageOutputDesc implements WritableComparable<AverageOutputDesc> {

    protected Text course;
    protected DoubleWritable score;
    protected IntWritable max;
    protected IntWritable min;

    public AverageOutputDesc() {

    }

    @Override
    public String toString() {
        return "course = " + course +
                ", score = " + score +
                ", max = " + max +
                ", min = " + min;
    }



    public IntWritable getMax() {
        return max;
    }

    public void setMax(IntWritable max) {
        this.max = max;
    }

    public IntWritable getMin() {
        return min;
    }

    public void setMin(IntWritable min) {
        this.min = min;
    }



    public AverageOutputDesc(Text course, DoubleWritable score, IntWritable max, IntWritable min) {
        this.course = course;
        this.score = score;
        this.max = max;
        this.min = min;
    }

    public Text getCourse() {
        return course;
    }

    public void setCourse(Text course) {
        this.course = course;
    }

    public DoubleWritable getScore() {
        return score;
    }
    public void set(Text course, DoubleWritable score) {
        this.course = course;
        this.score = score;
    }

    public void setScore(DoubleWritable score) {
        this.score = score;
    }



    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(course.toString());
        dataOutput.writeDouble(score.get());
        dataOutput.writeInt(max.get());
        dataOutput.writeInt(min.get());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        course = new Text(dataInput.readUTF());
        score = new DoubleWritable(dataInput.readDouble());
        max = new IntWritable(dataInput.readInt());
        min = new IntWritable(dataInput.readInt());
    }

    @Override
    public int compareTo(AverageOutputDesc o) {
        if (o == null) {
            throw new RuntimeException();
        }

        if (this.getCourse().compareTo(o.getCourse()) == 0) {
            return this.getScore().compareTo(o.getScore());
        }

        return this.getCourse().compareTo(o.getCourse());
    }


}
