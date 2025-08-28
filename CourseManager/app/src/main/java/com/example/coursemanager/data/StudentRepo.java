package com.example.coursemanager.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepo {
    private final StudentDao studentDao;
    private final ExecutorService executorService;

    public StudentRepo(Application application) {
        CourseDatabase db = CourseDatabase.getDatabase(application);
        studentDao = db.studentDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Student student) {
        executorService.execute(() -> studentDao.insert(student));
    }

    public void insert(Student student, int courseId) {
        executorService.execute(() -> {
            long id = studentDao.insert(student);
            int studentId = (int) (id != -1 ? id : student.getStudentId());
            CourseStudentJoin ref = new CourseStudentJoin(courseId, studentId);
            studentDao.insertCourseStudentJoin(ref);
        });
    }

    public void update(Student student) {
        executorService.execute(() -> studentDao.update(student));
    }

    public void delete(Student student) {
        executorService.execute(() -> studentDao.delete(student));
    }

    public void delete(Student student, int courseId) {
        executorService.execute(() -> {
            CourseStudentJoin ref = new CourseStudentJoin(courseId, student.getStudentId());
            studentDao.deleteCourseStudentJoin(ref);
        });
    }

    public void deleteFromAllCourses(int studentId) {
        executorService.execute(() -> studentDao.removeStudentFromAllCourses(studentId));
    }

    public LiveData<Student> getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    public LiveData<List<Student>> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public LiveData<CourseWithStudents> getStudentsForCourse(int courseId) {
        return studentDao.getStudentsForCourse(courseId);
    }

    public LiveData<StudentWithCourses> getCoursesForStudent(int studentId) {
        return studentDao.getCoursesForStudent(studentId);
    }

    public boolean isStudentAlreadyEnrolled(String matricNumber, int courseId) {
        Student existing = studentDao.getStudentByMatricNumber(matricNumber);
        return existing != null && studentDao.isStudentAlreadyEnrolled(existing.getStudentId(), courseId);
    }

    public boolean isStudentAlreadyExists(String userName) {
        Student existing = studentDao.getStudentByUserName(userName);
        return existing != null;
    }
}