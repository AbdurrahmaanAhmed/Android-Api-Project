package com.example.coursemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.R;
import com.example.coursemanager.data.Student;
import com.example.coursemanager.viewmodel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseDetailsActivity extends AppCompatActivity {

    private TextView courseCodeTextView, courseNameTextView, lecturerNameTextView;
    private RecyclerView studentRecyclerView;
    private StudentAdapter studentAdapter;
    private CourseViewModel courseViewModel;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // Initialize views
        courseCodeTextView = findViewById(R.id.courseCodeTextView);
        courseNameTextView = findViewById(R.id.courseNameTextView);
        lecturerNameTextView = findViewById(R.id.lecturerNameTextView);
        studentRecyclerView = findViewById(R.id.studentRecyclerView);
        FloatingActionButton fab = findViewById(R.id.addStudentFab);

        // Setup RecyclerView
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter();
        studentRecyclerView.setAdapter(studentAdapter);

        // Get course ID from intent
        courseId = getIntent().getIntExtra("COURSE_ID", -1);

        // Initialize ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Load course details
        courseViewModel.getCourseById(courseId).observe(this, course -> {
            if (course != null) {
                // Dynamically set the course details with labels
                courseCodeTextView.setText("Course Code: " + course.getCourseCode());
                courseNameTextView.setText("Course Name: " + course.getCourseName());
                lecturerNameTextView.setText("Lecturer Name: " + course.getLecturerName());
            }
        });

        // Load students for the course
        courseViewModel.getStudentsForCourse(courseId).observe(this, students -> {
            studentAdapter.setStudents(students);
        });

        // Set student click listener
        studentAdapter.setOnItemClickListener(position -> {
            Student student = studentAdapter.getStudentAt(position);
            showStudentOptionsDialog(student);
        });

        // FAB to add a new student
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
    }

    private void showStudentOptionsDialog(Student student) {
        String[] options = {"View Details", "Edit", "Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // View Details
                            Intent viewIntent = new Intent(this, StudentDetailsActivity.class);
                            viewIntent.putExtra("STUDENT_ID", student.getStudentId());
                            startActivity(viewIntent);
                            break;
                        case 1: // Edit
                            Intent editIntent = new Intent(this, EditStudentActivity.class);
                            editIntent.putExtra("STUDENT_ID", student.getStudentId());
                            startActivity(editIntent);
                            break;
                        case 2: // Remove from course
                            courseViewModel.removeStudentFromCourse(courseId, student.getStudentId());
                            break;
                    }
                })
                .show();
    }
}
