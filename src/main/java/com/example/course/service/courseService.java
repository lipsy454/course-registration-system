package com.example.course.service;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.io.FileReader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.course.Repository.courseRepo;
import com.example.course.dto.StudentRequest;
import com.example.course.Repository.CourseRegistryRepo;
import com.example.course.Repository.UserRepo;
import com.example.course.model.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class courseService implements ReportService{

    @Autowired
    private courseRepo courseRepo;

    @Autowired
    private CourseRegistryRepo courseRegistryRepo;

    @Autowired
    private UserRepo userRepo;

    // ================= COURSES =================

    public List<course> getAllCourses() {

        List<course> courses =
                courseRepo.findAll();

        List<course> result =
                new ArrayList<>();

        Iterator<course> iterator =
                courses.iterator();

        while(iterator.hasNext()) {

            result.add(iterator.next());
        }

        return result;
    }   
    public Map<String, Integer> getCourseWiseStudentCount() {

        List<CourseRegistry> students =
                courseRegistryRepo.findAll();

        Map<String, Integer> map =
                new HashMap<>();

        for(CourseRegistry s : students) {

            String courseName =
                    s.getCourseName().trim().toLowerCase();

            map.put(
                courseName,
                map.getOrDefault(courseName, 0) + 1
            );
        }

        return map;
    }   
    public Set<String> getUniqueStudentNames() {

        List<CourseRegistry> students =
                courseRegistryRepo.findAll();

        Set<String> names =
                new HashSet<>();

        for(CourseRegistry s : students) {

            String formattedName =
                    s.getName()
                     .trim()
                     .toLowerCase();

            names.add(formattedName);
        }

        return names;
    }    public List<CourseRegistry> getStudentsByCourse(
            String courseName) {

        return courseRegistryRepo.findAll()
                .stream()

                .filter(s ->
                    s.getCourseName()
                     .equalsIgnoreCase(courseName))

                .collect(Collectors.toList());
    }
    
    public String[][] studentDataArray() {

        List<CourseRegistry> students =
                courseRegistryRepo.findAll();

        String[][] data =
                new String[students.size()][4];

        for(int i = 0; i < students.size(); i++) {

            CourseRegistry s = students.get(i);

            data[i][0] = String.valueOf(s.getId());
            data[i][1] = s.getName();
            data[i][2] = s.getEmailId();
            data[i][3] = s.getCourseName();
        }

        return data;
    }


    // ================= SORTING =================

    public List<CourseRegistry> getEnrolledStudents() {

        List<CourseRegistry> students = courseRegistryRepo.findAll();

        students.sort((s1, s2) ->
            s1.getName().compareTo(s2.getName()));

        return students;
    }

    // ================= SEARCHING =================

  
    public CourseRegistry searchStudent(int id) {

        return courseRegistryRepo.findById(id)
                .orElse(null);
    }
    
    public List<CourseRegistry> searchStudent(String name) {

        return courseRegistryRepo.findByName(name);
    }

    public String readStudentFile() {

        StringBuilder data =
                new StringBuilder();

        try {

            FileReader reader =
                    new FileReader(
                            "students.txt");

            int character;

            while((character =
                    reader.read()) != -1) {

                data.append(
                        (char) character);
            }

            reader.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return "Student File Read Successfully\n\n"
                + data.toString();
    }  
    // ================= ENROLL =================

    public String enrollCourse(
            List<StudentRequest> students) {

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        students.size());

        CompletionService<String> completionService =
                new ExecutorCompletionService<>(executor);

        for(StudentRequest request : students) {

            completionService.submit(() -> {

                // ================= CHECK USER REGISTERED =================

                boolean userExists =
                        userRepo.existsByUsername(
                                request.getEmailId());

                if(!userExists) {

                    return request.getName()
                            + " Please Register First";
                }

                // ================= CHECK DUPLICATE ENROLLMENT =================

                boolean exists =
                        courseRegistryRepo
                        .existsByNameAndEmailIdAndCourseName(

                                request.getName(),

                                request.getEmailId(),

                                request.getCourseName());

                if(exists) {

                    return request.getName()
                            + " Already Enrolled";
                }

                // ================= SAVE DATABASE =================

                CourseRegistry student =
                        new CourseRegistry(

                                request.getName(),

                                request.getEmailId(),

                                request.getCourseName());

                courseRegistryRepo.save(student);

                // ================= FILE HANDLING =================

                try {

                    FileWriter writer =
                            new FileWriter(
                                    "students.txt",
                                    true);

                    writer.write(

                            student.getId() + " "

                            + student.getName() + " "

                            + student.getEmailId() + " "

                            + student.getCourseName()

                            + "\n");

                    writer.close();

                } catch(Exception e) {

                    e.printStackTrace();
                }

                return request.getName()
                        + " Enrollment Successful";
            });
        }

        StringBuilder response =
                new StringBuilder();

        try {

            for(int i = 0;
                    i < students.size();
                    i++) {

                Future<String> result =
                        completionService.take();

                response.append(result.get())
                        .append("\n");
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        executor.shutdown();

        return response.toString();
    }
    
    
    
    
    public boolean validateUser(String username,
                                String password) {

        return userRepo
                .findByUsernameAndPassword(username, password)
                != null;
    }

    // ================= REGISTER =================

    public void registerUser(String username,
                             String password) {

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        userRepo.save(user);
    }

    // ================= DELETE =================

    public void deleteEnrollment(int id) {

        courseRegistryRepo.deleteById(id);
    }

    // ================= GET STUDENT =================

    public CourseRegistry getStudentById(int id) {

        return courseRegistryRepo
                .findById(id)
                .orElse(null);
    }

    // ================= UPDATE =================

    public void updateEnrollment(int id,
                                 String name,
                                 String email,
                                 String courseName) {

        CourseRegistry cr =
            new CourseRegistry(name, email, courseName);

        cr.setId(id);

        courseRegistryRepo.save(cr);
    }

    // ================= DELETE STUDENT =================
    public String deleteStudent(int id) {

        boolean exists =
                courseRegistryRepo.existsById(id);

        if(!exists) {

            return "Student ID Not Found";
        }

        courseRegistryRepo.deleteById(id);

        return "Student Deleted Successfully";
    }
    // ================= REPORT GENERATION ==================    
    @Override
    public byte[] generateReport(String type) {

        List<CourseRegistry> students =
                courseRegistryRepo.findAll();

        try {

            // ================= PDF =================

            if(type.equalsIgnoreCase("pdf")) {

                ByteArrayOutputStream out =
                        new ByteArrayOutputStream();

                Document document = new Document();

                PdfWriter.getInstance(document, out);

                document.open();

                document.add(
                    new Paragraph("STUDENT REPORT"));

                for(CourseRegistry s : students) {

                    document.add(new Paragraph(
                            "ID : " + s.getId()
                          + " | NAME : " + s.getName()
                          + " | EMAIL : " + s.getEmailId()
                          + " | COURSE : "
                          + s.getCourseName()));
                }

                document.close();

                return out.toByteArray();
            }

            // ================= EXCEL =================

            else if(type.equalsIgnoreCase("excel")) {

                Workbook workbook =
                        new XSSFWorkbook();

                Sheet sheet =
                        workbook.createSheet("Students");

                int rowNum = 0;

                Row header =
                        sheet.createRow(rowNum++);

                header.createCell(0)
                        .setCellValue("ID");

                header.createCell(1)
                        .setCellValue("NAME");

                header.createCell(2)
                        .setCellValue("EMAIL");

                header.createCell(3)
                        .setCellValue("COURSE");

                for(CourseRegistry s : students) {

                    Row row =
                            sheet.createRow(rowNum++);

                    row.createCell(0)
                            .setCellValue(s.getId());

                    row.createCell(1)
                            .setCellValue(s.getName());

                    row.createCell(2)
                            .setCellValue(s.getEmailId());

                    row.createCell(3)
                            .setCellValue(s.getCourseName());
                }

                ByteArrayOutputStream out =
                        new ByteArrayOutputStream();

                workbook.write(out);

                workbook.close();

                return out.toByteArray();
            }

            // ================= CSV =================

            else {

                StringBuilder csv =
                        new StringBuilder();

                csv.append("ID,NAME,EMAIL,COURSE\n");

                for(CourseRegistry s : students) {

                    csv.append(s.getId()).append(",")
                       .append(s.getName()).append(",")
                       .append(s.getEmailId()).append(",")
                       .append(s.getCourseName()).append("\n");
                }

                return csv.toString().getBytes();
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}