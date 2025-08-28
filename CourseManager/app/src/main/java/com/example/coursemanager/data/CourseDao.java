package com.example.coursemanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insert(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY course_name ASC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE courseId = :courseId LIMIT 1")
    LiveData<Course> getCourseById(int courseId);

    @Transaction
    @Query("SELECT students.* FROM students " +
            "INNER JOIN course_student_join ON students.studentId = course_student_join.studentId " +
            "WHERE course_student_join.courseId = :courseId")
    LiveData<List<Student>> getStudentsForCourse(int courseId);

    @Query("DELETE FROM course_student_join WHERE courseId = :courseId AND studentId = :studentId")
    void removeStudentFromCourse(int courseId, int studentId);

    @Query("DELETE FROM courses WHERE courseId = :courseId")
    void deleteCourseById(int courseId);

    @Query("DELETE FROM courses")
    void deleteAll();


}