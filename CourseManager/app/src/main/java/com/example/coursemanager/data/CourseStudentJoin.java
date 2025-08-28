package com.example.coursemanager.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "course_student_join",
        primaryKeys = {"courseId", "studentId"},
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "courseId",
                        childColumns = "courseId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Student.class,
                        parentColumns = "studentId",
                        childColumns = "studentId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class CourseStudentJoin {

    @ColumnInfo(name = "courseId")
    public int courseId;

    @ColumnInfo(name = "studentId")
    public int studentId;

    public CourseStudentJoin(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }
}
