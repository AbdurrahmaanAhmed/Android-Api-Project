package com.example.coursemanager.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursemanager.R;
import com.example.coursemanager.data.Student;
import com.example.coursemanager.viewmodel.CourseViewModel;

public class AddStudentActivity extends AppCompatActivity {

    private CourseViewModel courseStudentViewModel; // Corrected ViewModel name
    private EditText studentNameEditText, studentEmailEditText, studentMatricEditText;

    private int courseId;  // The course ID to enroll the student in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize the ViewModel
        courseStudentViewModel = new ViewModelProvider(this).get(CourseViewModel.class); // Corrected ViewModel reference

        // Bind the EditText fields
        studentNameEditText = findViewById(R.id.studentNameEditText);
        studentEmailEditText = findViewById(R.id.studentEmailEditText);
        studentMatricEditText = findViewById(R.id.studentMatricEditText);

        // Assuming courseId is passed as an extra when starting this activity
        courseId = getIntent().getIntExtra("courseId", -1);

        // Set up the button click listener to add the student
        findViewById(R.id.addStudentButton).setOnClickListener(v -> addStudent());
    }

    private void addStudent() {
        // Get the values from the EditText fields
        String studentName = studentNameEditText.getText().toString().trim();
        String studentEmail = studentEmailEditText.getText().toString().trim();
        String studentMatric = studentMatricEditText.getText().toString().trim();

        // Check if the fields are empty
        if (studentName.isEmpty() || studentEmail.isEmpty() || studentMatric.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new student object
        Student student = new Student(studentName, studentEmail, studentMatric);

        // Insert the student into the database and associate with the course
        courseStudentViewModel.insertStudent(student, courseId); // Corrected ViewModel method

        // Show success message
        Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();

        // Close the activity and return to the previous one
        finish();
    }
}
