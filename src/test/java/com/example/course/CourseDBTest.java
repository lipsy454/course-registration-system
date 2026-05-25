package com.example.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.example.course.model.course;
import com.example.course.model.CourseRegistry;
import com.example.course.service.courseService;

@SpringBootTest
public class CourseDBTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private courseService courseService;

    // Fetch and print available courses
    @Test
    public void printAvailableCourses() {
        List<course> courses = courseService.getAllCourses();

        System.out.println("=== AVAILABLE COURSES ===");
        for (course c : courses) {
            System.out.println(
                "ID: " + c.getCourseId() +
                ", Name: " + c.getCourseName() +
                ", Trainer: " + c.getTrainer() +
                ", Duration: " + c.getDuration()
            );
        }
    }

    // Fetch and print enrolled students
    @Test
    public void printEnrolledStudents() {
        List<CourseRegistry> students = courseService.getEnrolledStudents();

        System.out.println("=== ENROLLED STUDENTS ===");
        for (CourseRegistry s : students) {
            System.out.println(
                "ID: " + s.getId() +
                ", Name: " + s.getName() +
                ", Email: " + s.getEmailId() +
                ", Course: " + s.getCourseName()
            );
        }
    }
}