package com.example.coursemanager.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class StudentWithCourses {
    @Embedded
    public Student student;

    @Relation(
            parentColumn = "studentId",
            entityColumn = "courseId",
            associateBy = @Junction(CourseStudentJoin.class)
    )
    public List<Course> courses;


    public List<Course> getCourses() {
        return courses;
    }

    // Optional: Also add a getter for the student if needed
    public Student getStudent() {
        return student;
    }
}
