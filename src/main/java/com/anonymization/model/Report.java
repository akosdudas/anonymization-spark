package com.anonymization.model;


import com.anonymization.mondrian.Quid;
import com.anonymization.mondrian.Record;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;

@Table(value="reports")
public class Report extends Record {

    @PrimaryKeyColumn(name = "id",ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int id;

    @Column(value = "message")
    private String message;

    @Column(value="anom")
    private boolean anom;

    @org.springframework.data.annotation.Transient
    private Quid<String> messageQuid;

    @Column(value="mqversion")
    private String mqversion;
    @Column(value="mqedition")
    private int mqedition;
    @Column(value="mquilang")
    private String mquilang;
    @Column(value="serialhash")
    private String serialhash;
    @Column(value="os")
    private String os;

    @org.springframework.data.annotation.Transient
    private Quid<String> osQuid;

    @Column(value="osversion")
    private String osversion;
    @Column(value="osarch")
    private String osarch;
    @Column(value="visiblememory")
    private Long visiblememory;

    @Column(value = "visiblememory_final")
    private String visiblememory_final;

    @org.springframework.data.annotation.Transient
    private Quid<Long> visiblememoryQuid;

    @Column(value="freememory")
    private Long freememory;

    @Column(value="freememory_final")
    private String freememory_final;


    @org.springframework.data.annotation.Transient
    private Quid<Long> freememoryQuid;

    @Column(value="cpuname")
    private String cpuname;
    @Column(value="netfxversions")
    private String netfxversions;
    @Column("originalhash")
    private String originalhash;
    @Column(value="timestamp")
    private String timestamp;
    @Column(value="mqarch")
    private String mqarch;
    @Column(value="exceptionobjecttype")
    private String exceptionobjecttype;
    @Column(value="stacktrace")
    private String stacktrace;

    @org.springframework.data.annotation.Transient
    private ArrayList<Quid> quids=new ArrayList<>();

    public void addQuids(){

        quids.add(freememoryQuid);
        quids.add(visiblememoryQuid);
//        quids.add(osQuid);
//        quids.add(messageQuid);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {

        this.message = message;
        this.messageQuid=new Quid<>(message);
    }

    public String getMqversion() {
        return mqversion;
    }

    public void setMqversion(String mqversion) {
        this.mqversion = mqversion;
    }

    public int getMqedition() {
        return mqedition;
    }

    public void setMqedition(int mqedition) {
        this.mqedition = mqedition;
    }

    public String getMquilang() {
        return mquilang;
    }

    public void setMquilang(String mquilang) {
        this.mquilang = mquilang;
    }

    public String getSerialhash() {
        return serialhash;
    }

    public void setSerialhash(String serialhash) {
        this.serialhash = serialhash;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {

        this.os = os;
        this.osQuid=new Quid<>(os);
    }

    public boolean isAnom() {
        return anom;
    }

    public void setAnom(boolean anom) {
        this.anom = anom;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public String getOsarch() {
        return osarch;
    }

    public void setOsarch(String osarch) {
        this.osarch = osarch;
    }

    public Long getVisiblememory() {
        return visiblememory;
    }

    public void setVisiblememory(Long visiblememory) {
        this.visiblememory = visiblememory;
        this.visiblememoryQuid=new Quid<>(visiblememory);
    }

    public Long getFreememory() {
        return freememory;
    }

    public void setFreememory(Long freememory) {

        this.freememory = freememory;
        this.freememoryQuid=new Quid<>(freememory);
    }

    public String getVisiblememory_final() {
        return visiblememory_final;
    }

    public void setVisiblememory_final(String visiblememory_final) {
        this.visiblememory_final = visiblememory_final;
    }

    public String getFreememory_final() {
        return freememory_final;
    }

    public void setFreememory_final(String freememory_final) {
        this.freememory_final = freememory_final;
    }

    public String getCpuname() {
        return cpuname;
    }

    public void setCpuname(String cpuname) {
        this.cpuname = cpuname;
    }

    public String getNetfxversions() {
        return netfxversions;
    }

    public void setNetfxversions(String netfxversions) {
        this.netfxversions = netfxversions;
    }

    public String getOriginalhash() {
        return originalhash;
    }

    public void setOriginalhash(String originalhash) {
        this.originalhash = originalhash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMqarch() {
        return mqarch;
    }

    public void setMqarch(String mqarch) {
        this.mqarch = mqarch;
    }

    public String getExceptionobjecttype() {
        return exceptionobjecttype;
    }

    public void setExceptionobjecttype(String exceptionobjecttype) {
        this.exceptionobjecttype = exceptionobjecttype;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    @Override
    public void setFinalData(int dim, String values) {
        if (dim==1) {
            finalData.put("freememory", values);
            freememory_final=values;
        }
        if (dim==2){
            finalData.put("visiblememory",values);
            visiblememory_final=values;
        }
    }

    @Override
    public int getForDim(int dimension) {
        return 0;
    }


    public Quid getQuidForDim(int dimension){
        if (dimension>quids.size()) throw new IndexOutOfBoundsException("Not so many quids");
        return quids.get(dimension-1);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                "finalData="+finalData+
//                ", message='" + message + '\'' +
//                ", mqversion='" + mqversion + '\'' +
//                ", mqedition=" + mqedition +
//                ", mquilang='" + mquilang + '\'' +
//                ", serialhash='" + serialhash + '\'' +
//                ", os='" + os + '\'' +
//                ", osversion='" + osversion + '\'' +
//                ", osarch='" + osarch + '\'' +
                ", visiblememory=" + visiblememory +
                ", freememory=" + freememory +
//                ", cpuname='" + cpuname + '\'' +
//                ", netfxversions='" + netfxversions + '\'' +
//                ", originalhash='" + originalhash + '\'' +
//                ", timestamp='" + timestamp + '\'' +
//                ", mqarch='" + mqarch + '\'' +
//                ", exceptionobjecttype='" + exceptionobjecttype + '\'' +
//                ", stacktrace='" + stacktrace + '\'' +
                '}';
    }
}
