package com.example.coursemanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students WHERE studentId = :studentId")
    LiveData<Student> getStudentById(int studentId);

    @Query("SELECT * FROM students")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM students WHERE userName = :userName LIMIT 1")
    Student getStudentByUserName(String userName);

    @Query("SELECT * FROM students WHERE matricNumber = :matricNumber LIMIT 1")
    Student getStudentByMatricNumber(String matricNumber);

    // Many-to-Many
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCourseStudentJoin(CourseStudentJoin join);

    @Delete
    void deleteCourseStudentJoin(CourseStudentJoin join);

    @Query("DELETE FROM course_student_join WHERE studentId = :studentId")
    void removeStudentFromAllCourses(int studentId);

    @Query("DELETE FROM course_student_join WHERE courseId = :courseId")
    void removeAllStudentsFromCourse(int courseId);

    @Query("SELECT EXISTS(SELECT 1 FROM course_student_join WHERE studentId = :studentId AND courseId = :courseId)")
    boolean isStudentAlreadyEnrolled(int studentId, int courseId);

    @Transaction
    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    LiveData<CourseWithStudents> getStudentsForCourse(int courseId);

    @Transaction
    @Query("SELECT * FROM students WHERE studentId = :studentId")
    LiveData<StudentWithCourses> getCoursesForStudent(int studentId);
}
