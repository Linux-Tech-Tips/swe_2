//class to contain constraints
//VERY ROUGH IMPLEMENTATION WILL BE CHANGED
package org.unixusergroup.tutorial_time.domain.scheduler;

public class Constraints {
    private final int studentLimit;
    private final int minTAs;
    private final int[] blockedTimes;

    /**
     * constructor, for stubbing reasons it's empty for now
     */
    public Constraints(int studentLimit, int minTAs, int[] blockedTimes) {
        this.studentLimit = studentLimit;
        this.minTAs = minTAs;
        this.blockedTimes = blockedTimes;
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

    public int[] getBlockedTimes() {
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