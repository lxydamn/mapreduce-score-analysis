package com.example.backend.mapred.utils;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AverageGroupComparator extends WritableComparator {

    public AverageGroupComparator() {
        super(AverageOutputDesc.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        AverageOutputDesc ia = (AverageOutputDesc) a;
        AverageOutputDesc ib = (AverageOutputDesc) b;
        return ib.getCourse().compareTo((ia.getCourse()));
    }
}
