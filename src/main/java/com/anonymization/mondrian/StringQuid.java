package com.anonymization.mondrian;

import com.anonymization.mondrian.Quid;

public class StringQuid extends Quid<String> {

    public StringQuid(String value) {
        super(value);
    }

    @Override
    public boolean biggerThen(Quid median) {
        return false;
    }
}
