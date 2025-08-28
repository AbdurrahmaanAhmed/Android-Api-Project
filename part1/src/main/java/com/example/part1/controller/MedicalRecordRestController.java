package com.example.part1.controller;


import com.example.part1.domain.Appointments;
import com.example.part1.domain.Record;
import com.example.part1.exception.ErrorInfo;
import com.example.part1.repo.AppointmentsRepo;
import com.example.part1.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordRestController {

    @Autowired
    private RecordRepo recordRepo;

    @Autowired
    private AppointmentsRepo appointmentsRepo;

    // 1st end point create a new medical record
    @PostMapping
    public ResponseEntity<?> createMedicalRecord(@RequestBody Map<String, Object> input, UriComponentsBuilder uriBuilder) {
        try {
            String dateStr = (String) input.get("recordDate");
            String diagnosis = (String) input.get("diagnosis");
            String treatment = (String) input.get("treatment");
            String notes = (String) input.get("notes");
            Long appointmentId = Long.valueOf(input.get("appointmentId").toString());

            if (appointmentId == null || dateStr == null || diagnosis == null || treatment == null) {
                return ResponseEntity.badRequest().body(new ErrorInfo(400, "Missing required fields"));
            }

            Appointments appointment = appointmentsRepo.findById(appointmentId).orElse(null);
            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorInfo(404, "Appointment not found"));
            }

            if (appointment.getRecord() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorInfo(409, "A record already exists for this appointment"));
            }

            Record record = new Record();
            record.setRecordDate(Timestamp.valueOf(dateStr));
            record.setDiagnosis(diagnosis);
            record.setTreatment(treatment);
            record.setNotes(notes);
            record.setAppointment(appointment);


            appointment.setRecord(record);

            recordRepo.save(record);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriBuilder.path("/api/medical-records/{id}").buildAndExpand(record.getId()).toUri());

            return new ResponseEntity<>( headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorInfo(400, "Invalid format or data"));
        }
    }

}

