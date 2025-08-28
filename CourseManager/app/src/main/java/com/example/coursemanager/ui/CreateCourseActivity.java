package com.example.coursemanager.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursemanager.R;
import com.example.coursemanager.data.Course;
import com.example.coursemanager.viewmodel.CourseViewModel;

public class CreateCourseActivity extends AppCompatActivity {

    private EditText courseCodeEditText, courseNameEditText, lecturerNameEditText;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        courseCodeEditText = findViewById(R.id.courseCodeEditText);
        courseNameEditText = findViewById(R.id.courseNameEditText);
        lecturerNameEditText = findViewById(R.id.lecturerNameEditText);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        findViewById(R.id.saveCourseButton).setOnClickListener(v -> {
            String courseCode = courseCodeEditText.getText().toString().trim();
            String courseName = courseNameEditText.getText().toString().trim();
            String lecturerName = lecturerNameEditText.getText().toString().trim();

            if (!courseCode.isEmpty() && !courseName.isEmpty() && !lecturerName.isEmpty()) {
                // Create and insert a new course
                Course course = new Course(courseCode, courseName, lecturerName);
                courseViewModel.insertCourse(course); // Save course
                Toast.makeText(CreateCourseActivity.this, "Course Added", Toast.LENGTH_SHORT).show();

                // Return to MainActivity
                finish();
            } else {
                Toast.makeText(CreateCourseActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
