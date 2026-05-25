package com.example.course.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.course.model.CourseRegistry;

@Repository
public interface CourseRegistryRepo extends JpaRepository<CourseRegistry,Integer>{
    List<CourseRegistry> findByName(String name);
    
    boolean existsByNameAndEmailIdAndCourseName(
            String name,
            String emailId,
            String courseName);



}
