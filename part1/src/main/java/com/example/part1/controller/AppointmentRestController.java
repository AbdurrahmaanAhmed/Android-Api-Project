package com.example.part1.controller;


import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Patient;
import com.example.part1.exception.ErrorInfo;
import com.example.part1.repo.AppointmentsRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.PatientRepo;
import com.example.part1.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.part1.domain.Record;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    @Autowired
    private AppointmentsRepo appointmentsRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private RecordRepo recordRepo;
    // 1st end point List all appointments
    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        List<Appointments> list = appointmentsRepo.findAll();
        if (list.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "No Appointments found"));
        return ResponseEntity.ok(list);
    }

    // 2nd end point create new appointments
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, Object> input, UriComponentsBuilder uriBuilder) {
        try {
            String status = (String) input.get("status");
            String notes = (String) input.get("notes");
            String dateStr = (String) input.get("appointmentDate");
            Long patientId = Long.valueOf(input.get("patientId").toString());
            Long doctorId = Long.valueOf(input.get("doctorId").toString());

            if (dateStr == null || status == null) {
                return ResponseEntity.badRequest().body(new ErrorInfo(400, "Date and status are required."));
            }

            Patient patient = patientRepo.findById(patientId).orElse(null);
            Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

            if (patient == null || doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorInfo(404, "Doctor or Patient not found"));
            }

            Appointments appointment = new Appointments();
            appointment.setAppointmentDate(Timestamp.valueOf(dateStr));
            appointment.setStatus(status);
            appointment.setNotes(notes);
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);

            appointmentsRepo.save(appointment);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriBuilder.path("/api/appointments/{id}")
                    .buildAndExpand(appointment.getId()).toUri());

            return new ResponseEntity<>( headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorInfo(400, "Invalid input format"));
        }
    }


    // 3rd end point retrieve a specific appointment by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable Long id) {
        Appointments appt = appointmentsRepo.findById(id).orElse(null);
        if (appt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Appointment not found with ID: " + id));
        }
        return ResponseEntity.ok(appt);
    }

    // 4th end point update a specific appointment by id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Appointments input) {
        Appointments appt = appointmentsRepo.findById(id).orElse(null);
        if (appt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "Appointment not found with ID: " + id));
        }


        appt.setPatient(patientRepo.findById(input.getPatient().getId()).orElse(null));
        appt.setDoctor(doctorRepo.findById(input.getDoctor().getId()).orElse(null));

        appt.setAppointmentDate(input.getAppointmentDate());
        appt.setStatus(input.getStatus());
        appt.setNotes(input.getNotes());

        appointmentsRepo.save(appt);
        return ResponseEntity.ok(appt);
    }


    // 5th end point delete specific appointment by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        Appointments appt = appointmentsRepo.findById(id).orElse(null);
        if (appt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Appointment not found with ID: " + id));
        }

        if (appt.getRecord() != null) {
            recordRepo.delete(appt.getRecord());
        }

        appointmentsRepo.delete(appt);
        return ResponseEntity.noContent().build();
    }

    // 6th end point retrieve the medical record for a specific appointment
    @GetMapping("{id}/medical-record")
    public ResponseEntity<?> getMedicalRecordForAppointment(@PathVariable Long id) {
        Appointments appt = appointmentsRepo.findById(id).orElse(null);
        if (appt == null || appt.getRecord() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "Medical record not found for appointment ID: " + id));
        }
        return ResponseEntity.ok(appt.getRecord());
    }





}
