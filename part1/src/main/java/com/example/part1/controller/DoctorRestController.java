package com.example.part1.controller;



import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.exception.ErrorInfo;
import com.example.part1.repo.AppointmentsRepo;
import com.example.part1.repo.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorRestController {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private AppointmentsRepo appointmentsRepo;

    // 1st end point list all doctors
    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorInfo(404, "No Doctor found"));
        }
        return ResponseEntity.ok(doctors);
    }

    // 2nd end point create a new doctor
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor, UriComponentsBuilder uriBuilder) {
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorInfo(400, "Doctor name is required."));
        }

        doctorRepo.save(doctor);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/api/doctors/{id}").buildAndExpand(doctor.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // 3rd end point retrieve a specific doctor by id
    @GetMapping("{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Doctor not found with ID: " + id));
        }
        return ResponseEntity.ok(doctor);
    }

    //4th end point update a specific doctor by id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody Doctor updated) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Doctor not found with ID: " + id));
        }

        if (updated.getName() == null || updated.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorInfo(400, "Doctor name is required."));
        }

        doctor.setName(updated.getName());
        doctor.setEmail(updated.getEmail());
        doctor.setPhoneNumber(updated.getPhoneNumber());
        doctor.setSpecialisation(updated.getSpecialisation());

        doctorRepo.save(doctor);
        return ResponseEntity.ok(doctor);
    }

    //5th end point delete doctor by id
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Doctor not found with ID: " + id));
        }

        List<Appointments> appointments = appointmentsRepo.findByDoctor(doctor);
        for (Appointments appt : appointments) {
            appointmentsRepo.delete(appt);
        }

        doctorRepo.delete(doctor);
        return ResponseEntity.noContent().build();
    }

    // 6th end point list all appointments for a specific doctor
    @GetMapping("{id}/appointments")
    public ResponseEntity<?> getAppointmentsForDoctor(@PathVariable Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Doctor not found with ID: " + id));
        }

        List<Appointments> appointments = appointmentsRepo.findByDoctor(doctor);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(appointments);
    }







}
