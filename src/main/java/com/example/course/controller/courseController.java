package com.example.course.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.course.Repository.CourseRegistryRepo;
import com.example.course.dto.StudentRequest;
import com.example.course.model.CourseRegistry;
import com.example.course.service.courseService;

@RestController
@RequestMapping("/api")
public class courseController {

    @Autowired
    private courseService service;

    @Autowired
    private CourseRegistryRepo registryRepo;

    // ================= LOGIN =================

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        if (service.validateUser(username, password)) {
            return "Login Successful";
        } else {
            return "Invalid Username or Password";
        }
    }

    // ================= SIGNUP =================

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password) {

        service.registerUser(username, password);
        return "Registration Successful";
    }

    // ================= COURSES =================

    @GetMapping("/courses")                     //Array List
    public List<?> showCourses() {
        return service.getAllCourses();
    }
    @GetMapping("/course-count")                  //hash map
    public Map<String, Integer> courseCount() {

        return service.getCourseWiseStudentCount();
    }
    @GetMapping("/unique-students")            //hash set
    public Set<String> uniqueStudents() {

        return service.getUniqueStudentNames();
    }
    @GetMapping("/students/course")               //streams
    public List<CourseRegistry> studentsByCourse(
            @RequestParam String courseName) {

        return service.getStudentsByCourse(courseName);
    }
    @GetMapping("/student-array")                  //2d array
    public String[][] studentArray() {

        return service.studentDataArray();
    }

    // ================= ENROLL =================

    @PostMapping("/courses/register")
    public String enroll(

            @RequestBody
            List<StudentRequest> students) {

        return service.enrollCourse(
                students);
    }
    // ================= UPDATE =================

    @PutMapping("/update")
    public String update(@RequestParam int id,
                         @RequestParam String name,
                         @RequestParam String emailId,
                         @RequestParam String courseName) {

        service.updateEnrollment(id, name, emailId, courseName);
        return "Enrollment Updated Successfully";
    }

    // ================= STUDENTS =================

    @GetMapping("/students")
    public List<?> studentsPage() {
        return service.getEnrolledStudents();
    }

    // ================= GET STUDENT BY ID =================

    @GetMapping("/student/{id}")
    public CourseRegistry getStudent(@PathVariable int id) {
        return service.getStudentById(id);
    }
    //method overloading

    @GetMapping("/search/name")
    public Object searchStudent(
            @RequestParam String name) {

        List<CourseRegistry> students =
                service.searchStudent(name);

        if(students.isEmpty()) {

            return "Student Name Not Found";
        }

        return students;
    }    
    @GetMapping("/search/id")
    public Object searchById(
            @RequestParam int id) {

        CourseRegistry student =
                service.searchStudent(id);

        if(student == null) {

            return "Student ID Not Found";
        }

        return student;
    }  
    @GetMapping("/read-file")
    public String readStudentFile() {

        return service.readStudentFile();
    }
    // ================= DELETE =================

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(
            @PathVariable int id) {

        return service.deleteStudent(id);
    }}