package com.example.coursemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.data.Course;
import com.example.coursemanager.ui.CourseAdapter;
import com.example.coursemanager.ui.StudentAdapter;
import com.example.coursemanager.viewmodel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private CourseViewModel courseViewModel;  // ✅ Use only CourseViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up RecyclerViews
        RecyclerView courseRecyclerView = findViewById(R.id.courseRecyclerView);
        RecyclerView studentRecyclerView = findViewById(R.id.studentRecyclerView);

        CourseAdapter courseAdapter = new CourseAdapter();
        StudentAdapter studentAdapter = new StudentAdapter();

        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseRecyclerView.setAdapter(courseAdapter);
        studentRecyclerView.setAdapter(studentAdapter);

        // ✅ Initialize unified ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // ✅ Observe courses
        courseViewModel.getAllCourses().observe(this, courses -> {
            Log.d("MainActivity", "Courses size: " + courses.size());
            courseAdapter.setCourses(courses);
        });

        courseViewModel.getAllCourses().observe(this, courses -> {
            // Update the adapter with the new list
            courseAdapter.setCourses(courses);
        });

        courseViewModel.getAllCourses().observe(this, courses -> {
            // Update the RecyclerView with new data
            courseAdapter.setCourses(courses);
        });



        // ✅ Observe students
        courseViewModel.getAllStudents().observe(this, students -> {
            Log.d("MainActivity", "Students size: " + students.size());
            studentAdapter.setStudents(students);
        });

        // Add course FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.coursemanager.ui.CreateCourseActivity.class);
            startActivity(intent);
        });

        // Long press to delete course
        courseAdapter.setOnItemLongClickListener(position -> {
            Course course = courseAdapter.getCourseAt(position);
            courseViewModel.deleteCourse(course); // ✅ Correct method name
            Toast.makeText(MainActivity.this, "Course deleted", Toast.LENGTH_SHORT).show();
        });

        // View course details on click
        courseAdapter.setOnItemClickListener(position -> {
            Course course = courseAdapter.getCourseAt(position);
            Intent intent = new Intent(MainActivity.this, com.example.coursemanager.ui.CourseDetailsActivity.class);
            intent.putExtra("COURSE_ID", course.getCourseId());
            startActivity(intent);
        });
    }
}
