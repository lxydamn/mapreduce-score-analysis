package com.example.backend.mapred.utils;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SameOutput implements WritableComparable<SameOutput> {

    protected Text course;

    protected DoubleWritable score;

    public Text getCourse() {
        return course;
    }

    public void setCourse(Text course) {
        this.course = course;
    }

    public DoubleWritable getScore() {
        return score;
    }

    public void setScore(DoubleWritable score) {
        this.score = score;
    }

    public SameOutput() {}

    public SameOutput(Text course, DoubleWritable score) {
        this.course = course;
        this.score = score;
    }

    @Override
    public String toString() {
        return "course=" + course +
                ", score=" + score;
    }

    @Override
    public int compareTo(SameOutput o) {
        if (o == null) {
            throw new RuntimeException();
        }
        if (this.getCourse().compareTo(o.getCourse()) == 0) {
            return this.getScore().compareTo(o.getScore());
        }
        return this.getCourse().compareTo(o.getCourse());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(course.toString());
        dataOutput.writeDouble(score.get());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        course = new Text(dataInput.readUTF());
        score = new DoubleWritable(dataInput.readDouble());
    }
}
