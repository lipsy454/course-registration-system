package com.example.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.*;

import com.example.course.service.courseService;
import com.example.course.model.course;

import java.util.List;

@SpringBootTest
public class CourseServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private courseService courseService;

    @BeforeClass
    public void beforeClass() {
        System.out.println("Before Class - Setup started");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("Before Method");
    }

    @Test
    public void testAvailableCourses() {
        List<course> courses = courseService.getAllCourses();
        Assert.assertNotNull(courses);
    }

    @Test

    @AfterMethod
    public void afterMethod() {
        System.out.println("After Method");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("After Class");
    }
}