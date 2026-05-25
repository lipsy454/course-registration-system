package com.example.course.service;

import java.io.IOException;

public interface ReportService {
	
    byte[] generateReport(String type) throws IOException;
}