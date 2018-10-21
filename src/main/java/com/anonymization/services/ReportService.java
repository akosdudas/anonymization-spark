package com.anonymization.services;

import com.anonymization.model.Report;
import com.anonymization.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    ReportRepository repository;

    public Report save(final Report data) {
        return repository.save(data);
    }

    public List<Report> saveAll(final List<Report> reportRecords){
        return repository.saveAll(reportRecords);
    }

    public Optional<Report> findOne(int id) {

        return repository.findById(id);

    }

    public List<Report> findAll() {
        List<Report> list = new ArrayList<>();
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
