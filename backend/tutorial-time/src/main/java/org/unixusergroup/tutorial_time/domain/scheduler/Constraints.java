//class to contain constraints
//VERY ROUGH IMPLEMENTATION WILL BE CHANGED
package org.unixusergroup.tutorial_time;

public class Constraints {
    private int studentLimit;
    private int minTAs;
    private int[] blockedTimes;

    /**
     * constructor, for stubbing reasons it's empty for now
     */
    public Constraints() {

    }

    public int getStudentLimit() {
        return studentLimit;
    }

    /**
     * checks if a student limit has been provided
     * @return true if yes, false if no
     */
    public boolean studentLimit() {
        if(studentLimit==null) {
            return false;
        }
        return true;
    }

    public int getBlockedTimes() {
        return blockedTimes;
    }

    /**
     * checks if any times to block off have been provided
     * @return true if yes, false if no
     */
    public boolean blockedTimes() {
        if(blockedTimes==null) {
            return false;
        }
        return true;
    }

    //TODO: make methods for TAs
}