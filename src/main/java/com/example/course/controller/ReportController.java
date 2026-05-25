package com.example.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.course.service.courseService;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private courseService service;

    @GetMapping("/{type}")
    public ResponseEntity<byte[]> generateReport(
            @PathVariable String type) {

        byte[] data = service.generateReport(type);

        String fileName = "student_report.";

        MediaType mediaType;

        // ================= PDF =================

        if(type.equalsIgnoreCase("pdf")) {

            fileName += "pdf";

            mediaType = MediaType.APPLICATION_PDF;
        }

        // ================= EXCEL =================

        else if(type.equalsIgnoreCase("excel")) {

            fileName += "xlsx";

            mediaType = MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }

        // ================= CSV =================

        else {

            fileName += "csv";

            mediaType = MediaType.TEXT_PLAIN;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .contentType(mediaType)
                .body(data);
    }
}