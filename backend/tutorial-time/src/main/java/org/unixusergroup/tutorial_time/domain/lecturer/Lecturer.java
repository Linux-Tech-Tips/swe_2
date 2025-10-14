package org.unixusergroup.tutorial_time.domain.lecturer;

import org.unixusergroup.tutorial_time.domain.Tutorial.TimeTable;
import org.unixusergroup.tutorial_time.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;

@Entity
public class Lecturer extends User {
    @Id
    public int staffID;

    public Lecturer (String name, int staffID) {
        this.name = name;
        this.staffID = staffID;
        this.courses = new ArrayList<String>();
        this.timeTable = (TimeTable) TimeTable.generateTimeTable();
    }
}
