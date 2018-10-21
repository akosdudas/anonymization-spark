package com.anonymization.repositories;

import com.anonymization.model.Report;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ReportRepository extends CassandraRepository<Report,Integer> {
}
