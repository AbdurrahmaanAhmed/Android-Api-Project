package com.example.coursemanager.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.R;
import com.example.coursemanager.viewmodel.CourseViewModel;

public class StudentDetailsActivity extends AppCompatActivity {
    private TextView studentNameTextView, studentEmailTextView, studentMatricTextView;
    private RecyclerView coursesRecyclerView;
    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        studentNameTextView = findViewById(R.id.studentNameTextView);
        studentEmailTextView = findViewById(R.id.studentEmailTextView);
        studentMatricTextView = findViewById(R.id.studentMatricTextView);
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);

        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter();
        coursesRecyclerView.setAdapter(courseAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class); // Fixed ViewModel

        int studentId = getIntent().getIntExtra("STUDENT_ID", -1);

        // Load student details
        courseViewModel.getStudentById(studentId).observe(this, student -> {
            if (student != null) {
                studentNameTextView.setText(student.getName());
                studentEmailTextView.setText(student.getEmail());
                studentMatricTextView.setText(student.getMatricNumber());
            }
        });

        // Load courses the student is enrolled in
        courseViewModel.getCoursesForStudent(studentId).observe(this, studentWithCourses -> {
            if (studentWithCourses != null && studentWithCourses.getCourses() != null) {
                courseAdapter.setCourses(studentWithCourses.getCourses()); // Use getter method
            }
        });
    }
}
