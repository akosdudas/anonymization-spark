package com.anonymization.repositories;

import com.anonymization.model.MedicalRecord;
import org.springframework.data.cassandra.repository.CassandraRepository;


public interface MedicalRecordRepository extends CassandraRepository<MedicalRecord,Integer> {
}
