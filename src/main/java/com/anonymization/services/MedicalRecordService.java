package com.anonymization.services;

import com.anonymization.model.MedicalRecord;
import com.anonymization.repositories.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    MedicalRecordRepository repository;

    public MedicalRecord save(final MedicalRecord data) {
        return repository.save(data);
    }

    public List<MedicalRecord> saveAll(final List<MedicalRecord> medicalRecords){
        return repository.saveAll(medicalRecords);
    }

    public Optional<MedicalRecord> findOne(int id) {

        return repository.findById(id);
    }

    public List<MedicalRecord> findAll() {
        List<MedicalRecord> list = new ArrayList<>();
        repository.findAll().forEach(e -> list.add(e));
        return list;
    }

    public void delete(final int id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }

    public boolean exists(final int id) {
        return repository.existsById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
