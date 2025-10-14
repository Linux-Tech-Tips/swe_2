package com.example.tutorial_time.domain.lecturer;

import com.example.tutorial_time.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Lecturer extends User {
    @Id
    public int staffID;
    public Lecturer(String name, int staffID) {
        super(name, staffID);
    }

}
