package com.anonymization.mondrian;

import java.io.Serializable;

public abstract class Quid<T> implements Serializable {

    private  T value;

    public Quid(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getType(){
        return value.getClass().getTypeName();
    }

    abstract public boolean biggerThen(Quid median);
}
