package com.anonymization.mondrian;

import com.anonymization.mondrian.Quid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Record implements Serializable {

    /**
     * Storing final data, for each quid.
     */
    @org.springframework.data.annotation.Transient
    protected HashMap<String,String> finalData=new HashMap<>();

    public abstract void setFinalData(int dim,String values);

    public abstract Quid getQuidForDim(int dimension);

    public abstract ArrayList<Quid> getQuids();

    public abstract Quid getSuppressedDim(int dimension);

    public abstract void setFinalPressed(int dim,String value);
}
