package com.example.coursemanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.coursemanager.data.Course;
import com.example.coursemanager.data.CourseRepo;
import com.example.coursemanager.data.Student;
import com.example.coursemanager.data.StudentRepo;
import com.example.coursemanager.data.StudentWithCourses;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepo courseRepository;
    private final StudentRepo studentRepository;
    private final LiveData<List<Course>> allCourses;
    private final LiveData<List<Student>> allStudents;

    public CourseViewModel(Application application) {
        super(application);
        courseRepository = new CourseRepo(application);
        studentRepository = new StudentRepo(application);
        allCourses = courseRepository.getAllCourses();
        allStudents = studentRepository.getAllStudents();
    }

    // Course-related methods

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<Course> getCourseById(int courseId) {
        return courseRepository.getCourseById(courseId);
    }

    public LiveData<List<Student>> getStudentsForCourse(int courseId) {
        return courseRepository.getStudentsForCourse(courseId);
    }

    public void insertCourse(Course course) {
        courseRepository.insert(course);
    }

    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }



    // Student-related methods

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public LiveData<Student> getStudentById(int studentId) {
        return studentRepository.getStudentById(studentId);
    }

    public LiveData<StudentWithCourses> getCoursesForStudent(int studentId) {
        return studentRepository.getCoursesForStudent(studentId);
    }

    public void insertStudent(Student student, int courseId) {
        studentRepository.insert(student, courseId); // Insert student and associate with a course
    }

    public void updateStudent(Student student) {
        studentRepository.update(student);
    }

    public void deleteStudent(Student student, int courseId) {
        studentRepository.delete(student, courseId); // Remove student from course
    }

    // Helper methods for enrollment and existence checks
    public boolean isStudentAlreadyEnrolled(String matricNumber, int courseId) {
        return studentRepository.isStudentAlreadyEnrolled(matricNumber, courseId);
    }

    public boolean isStudentAlreadyExists(String userName) {
        return studentRepository.isStudentAlreadyExists(userName);
    }

    public void removeStudentFromCourse(int courseId, int studentId) {
        courseRepository.removeStudentFromCourse(courseId, studentId);
    }
}
