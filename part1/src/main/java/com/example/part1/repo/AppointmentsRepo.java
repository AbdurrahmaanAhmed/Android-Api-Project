package com.example.part1.repo;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentsRepo extends JpaRepository<Appointments, Long> {
    List<Appointments> findByPatient(Patient patient);
    List<Appointments> findByDoctor(Doctor doctor);

}
