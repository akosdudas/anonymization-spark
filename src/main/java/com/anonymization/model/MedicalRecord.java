package com.anonymization.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value="medical")
public class MedicalRecord {

    @PrimaryKeyColumn(name = "id",ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private long id;
    @Column(value="age")
    private int age;
    @Column(value = "gender")
    private String gender;
    @Column(value = "iranyitoszam")
    private int iranyitoszam;
    @Column(value="age_final")
    private String age_final;
    @Column(value="iranyitoszam_final")
    private String iranyitoszam_final;
    @Column(value="disease")
    private String disease;

    public MedicalRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIranyitoszam() {
        return iranyitoszam;
    }

    public void setIranyitoszam(int iranyitoszam) {
        this.iranyitoszam = iranyitoszam;
    }

    public String getAge_final() {
        return age_final;
    }

    public void setAge_final(String age_final) {
        this.age_final = age_final;
    }

    public String getIranyitoszam_final() {
        return iranyitoszam_final;
    }

    public void setIranyitoszam_final(String iranyitoszam_final) {
        this.iranyitoszam_final = iranyitoszam_final;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public MedicalRecord(long id, int age, String gender, int iranyitoszam, String disease) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.iranyitoszam = iranyitoszam;
        this.disease = disease;
    }
}
