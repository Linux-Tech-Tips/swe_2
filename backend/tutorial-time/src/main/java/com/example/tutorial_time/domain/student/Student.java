package com.example.tutorial_time.domain.student;

import com.example.tutorial_time.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student extends User {

    @Id
    private int studentID;

    public Student(String name, int studentId) {
        super(name, studentId);
    }
}
