package com.example.course.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.course.model.course;
@Repository
public interface courseRepo extends JpaRepository<course,String>  {

}
