package com.anonymization.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value="reports")
public class Report {

    @PrimaryKeyColumn(name = "id",ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int id;

    @Column(value = "message")
    private String message;

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
    @Column(value="osversion")
    private String osversion;
    @Column(value="osarch")
    private String osarch;
    @Column(value="visiblememory")
    private Long visiblememory;
    @Column(value="freememory")
    private Long freememory;
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
    }

    public Long getFreememory() {
        return freememory;
    }

    public void setFreememory(Long freememory) {
        this.freememory = freememory;
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
}
