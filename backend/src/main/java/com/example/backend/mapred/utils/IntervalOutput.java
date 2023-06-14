package com.example.backend.mapred.utils;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntervalOutput implements WritableComparable<IntervalOutput> {

    protected Text course;

    protected IntWritable type;

    public Text getCourse() {
        return course;
    }

    public void setCourse(Text course) {
        this.course = course;
    }

    public IntWritable getType() {
        return type;
    }

    public void setType(IntWritable type) {
        this.type = type;
    }

    public IntervalOutput() {

    }
    public IntervalOutput(Text course, IntWritable type) {
        this.course = course;
        this.type = type;
    }

    @Override
    public String toString() {
        return "course=" + course +
                ", type=" + type;
    }

    @Override
    public int compareTo(IntervalOutput o) {
        if (o == null) {
            throw new RuntimeException();
        }
        if (this.getCourse().compareTo(o.getCourse()) == 0) {
            return this.getType().compareTo(o.getType());
        }
        return this.getCourse().compareTo(o.getCourse());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(course.toString());
        dataOutput.writeInt(type.get());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        course = new Text(dataInput.readUTF());
        type = new IntWritable(dataInput.readInt());
    }
}
