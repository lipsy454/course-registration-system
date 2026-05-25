package com.example.course.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.course.model.User;

public interface UserRepo extends JpaRepository<User, String> {
    User findByUsernameAndPassword(
            String username,
            String password);

    boolean existsByUsername(String username);
}