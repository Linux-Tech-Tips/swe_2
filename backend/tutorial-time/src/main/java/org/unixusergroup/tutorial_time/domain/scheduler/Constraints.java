/**
 * class for constraints on the scheduling algorithm
 * @author Cedar
 * TODO: FIGURE OUT HOW HE IS CREATED
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import org.unixusergroup.tutorial_time.application.exceptions.IllegalConstraintsException;

public class Constraints {
    private final int studentLimit;
    private final int minTAs;
    private final int[] blockedTimes;

    /**
     * constructor
     * gets the data from the frontend, which ensures that there are default values for each attribute and that all data is passed in together
     * @param studentLimit the maximum number of students that can be in one tutorial, 0 means no limit
     * @param minTAs the minimum number of TAs that must be in each tutorial, 0 means no limit
     * @param blockedTimes time that should not be considered
     */
    public Constraints(int studentLimit, int minTAs, int[] blockedTimes) throws IllegalConstraintsException{
        if(studentLimit<0) {
            throw new IllegalConstraintsException("The student limit must be 0 or more");
        }
        if(minTAs<0) {
            throw new IllegalConstraintsException("The TA minimum must be 0 or more");
        }
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
        return !(blockedTimes.length==0);
    }

    public int getMinTAs() {
        return minTAs;
    }

    /**
     * checks if a minimum number of TAs has been provided
     * @return true if yes, false if no
     */
    public boolean minTAs() {
        return minTAs!=0;
    }
}