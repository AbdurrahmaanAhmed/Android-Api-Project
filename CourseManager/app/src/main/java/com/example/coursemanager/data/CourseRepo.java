package com.example.coursemanager.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CourseRepo {
    private final CourseDao courseDao;
    private final LiveData<List<Course>> allCourses;

    public CourseRepo(Application application) {
        CourseDatabase db = CourseDatabase.getDatabase(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<Course> getCourseById(int courseId) {
        return courseDao.getCourseById(courseId);
    }

    public LiveData<List<Student>> getStudentsForCourse(int courseId) {
        return courseDao.getStudentsForCourse(courseId);
    }

    public void removeStudentFromCourse(int courseId, int studentId) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.removeStudentFromCourse(courseId, studentId);
        });
    }

    public void insert(Course course) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insert(course);
        });
    }

    public void delete(Course course) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.delete(course);
        });
    }
}
