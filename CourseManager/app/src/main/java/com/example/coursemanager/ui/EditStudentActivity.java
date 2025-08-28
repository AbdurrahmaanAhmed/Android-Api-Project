package com.example.coursemanager.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursemanager.R;
import com.example.coursemanager.data.Student;
import com.example.coursemanager.viewmodel.CourseViewModel;

public class EditStudentActivity extends AppCompatActivity {
    private EditText studentNameEditText, studentEmailEditText, studentMatricEditText;
    private CourseViewModel courseViewModel;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        studentNameEditText = findViewById(R.id.studentNameEditText);
        studentEmailEditText = findViewById(R.id.studentEmailEditText);
        studentMatricEditText = findViewById(R.id.studentMatricEditText);

        studentId = getIntent().getIntExtra("STUDENT_ID", -1);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Observe the student data
        courseViewModel.getStudentById(studentId).observe(this, student -> {
            if (student != null) {
                studentNameEditText.setText(student.getName());
                studentEmailEditText.setText(student.getEmail());
                studentMatricEditText.setText(student.getMatricNumber());
            }
        });

        findViewById(R.id.saveStudentButton).setOnClickListener(v -> {
            String name = studentNameEditText.getText().toString().trim();
            String email = studentEmailEditText.getText().toString().trim();
            String matric = studentMatricEditText.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty() && !matric.isEmpty()) {
                Student updatedStudent = new Student(name, email, matric);
                updatedStudent.setStudentId(studentId); // Set the ID to update the correct student
                courseViewModel.updateStudent(updatedStudent); // ✅ fixed method name
                finish(); // Go back to previous screen
            } else {
                Toast.makeText(EditStudentActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
