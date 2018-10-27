package com.anonymization.repositories;

import com.anonymization.model.Report;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

public interface ReportRepository extends CassandraRepository<Report,Integer> {
}
