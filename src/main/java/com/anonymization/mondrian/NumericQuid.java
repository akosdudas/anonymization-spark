package com.anonymization.mondrian;

import com.anonymization.mondrian.Quid;

public class NumericQuid extends Quid<Long> {
    public NumericQuid(Long value) {
        super(value);
    }


    @Override
    public boolean biggerThen(Quid median) {
        NumericQuid that=(NumericQuid)median;
        return this.getValue()>that.getValue();
    }




}
