package com.example.part1.repo;
import com.example.part1.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepo extends JpaRepository<Record, Long> {
}
