/**
 * class for constraints on the scheduling algorithm
 * @author Cedar
 * TODO: FIGURE OUT HOW HE IS CREATED
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import java.util.ArrayList;

//TODO: is he a data transfer object??
public class Constraints {
    private int studentLimit;
    private int minTAs;
    private ArrayList<Integer> blockedTimes;

    /**
     * constructor
     */
    public Constraints() {
        this.studentLimit = 0;
        this.minTAs = 0;
        this.blockedTimes = null;
    }

    public void setStudentLimit(int studentLimit) {
        if(studentLimit<1) {
            throw new IllegalArgumentException("The tutorial size limit cannot be zero or negative");
        }
        this.studentLimit = studentLimit;
    }
    public int getStudentLimit() {
        return studentLimit;
    }

    /**
     * checks if a student limit has been provided
     * @return true if yes, false if no
     */
    public boolean studentLimit() {
        return studentLimit != 0;
    }

    public void setBlockedTimes(ArrayList<Integer> blockedTimes) {
        this.blockedTimes = blockedTimes;
    }

    public ArrayList<Integer> getBlockedTimes() {
        return blockedTimes;
    }

    /**
     * checks if any times to block off have been provided
     * @return true if yes, false if no
     */
    public boolean blockedTimes() {
        return blockedTimes != null;
    }

    //TODO: make methods for TAs
}