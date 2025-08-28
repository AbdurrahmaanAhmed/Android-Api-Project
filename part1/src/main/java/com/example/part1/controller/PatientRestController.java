package com.example.part1.controller;


import com.example.part1.domain.Appointments;
import com.example.part1.domain.Patient;
import com.example.part1.exception.ErrorInfo;
import com.example.part1.repo.AppointmentsRepo;
import com.example.part1.repo.PatientRepo;
import com.example.part1.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.part1.domain.Record;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {
    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private AppointmentsRepo appointmentsRepo;

    @Autowired
    private RecordRepo recordRepo;

    // 1st end point list all patients
    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients= patientRepo.findAll();
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "No patients found"));
        }
        return ResponseEntity.ok(patients);
    }

    // 2nd end point create patients
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient, UriComponentsBuilder uriCBuilder) {
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorInfo(HttpStatus.BAD_REQUEST.value(), "Name is required."),
                    HttpStatus.BAD_REQUEST
            );
        }

        patientRepo.save(patient);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriCBuilder.path("/api/patients/{id}").buildAndExpand(patient.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // 3rd end point retrieve a patient by id
    @GetMapping("{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "Patient not found with ID " + id));
        }
        return ResponseEntity.ok(patient);
    }

    // 4th end point update a specific patient by id
    @PutMapping("{id}")
    public ResponseEntity<?> modifyPatient(@PathVariable Long id, @RequestBody Patient updated) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "No patient found with ID " + id));
        }

        if (updated.getName() == null || updated.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorInfo(400, "Name is required.")
            );
        }

        patient.setName(updated.getName());
        patient.setEmail(updated.getEmail());
        patient.setPhoneNumber(updated.getPhoneNumber());
        patient.setAddress(updated.getAddress());

        patientRepo.save(patient);
        return ResponseEntity.ok(patient);
    }

    // 5th end point delete a specific patient by id
    @DeleteMapping("{id}")
    public ResponseEntity<?> removePatient(@PathVariable Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "Patient not found with ID " + id));
        }

        List<Appointments> appts = appointmentsRepo.findByPatient(patient);
        for (Appointments appt : appts) {
            if (appt.getRecord() != null) {
                recordRepo.delete(appt.getRecord());
            }
            appointmentsRepo.delete(appt);
        }

        patientRepo.delete(patient);
        return ResponseEntity.noContent().build();
    }

    // 6th end point list all appointments for specific patient
    @GetMapping("{id}/appointments")
    public ResponseEntity<?> getPatientAppointments(@PathVariable Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "Patient not found with ID " + id));
        }

        List<Appointments> appointments = patient.getAppointments();
        if (appointments == null || appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(appointments);
    }

    // 7th end point list all medical records for a specific patient
    @GetMapping("{id}/medical-records")
    public ResponseEntity<?> getPatientRecords(@PathVariable Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "Patient not found with ID " + id));
        }

        List<Record> records = new ArrayList<>();
        List<Appointments> appts = patient.getAppointments();

        if (appts != null) {
            for (Appointments appt : appts) {
                if (appt.getRecord() != null) {
                    records.add(appt.getRecord());
                }
            }
        }

        if (records.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(records);
    }
}



