/**
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.timetable;

/** Enumeration signifying whether a user is unavailable or available */
public enum Availability {
    /** Student being unavailable, maps to 0 */
    UNAVAILABLE(0),
    /** Student being available, maps to 1 */
    AVAILABLE(1);

    /** The value of a specific Availability-typed variable */
    private final int value;

    /** Private constructor for Availability values */
    private Availability(final int value) {
        this.value = value;
    }

    /** Public accessor for the numeric value that the enum maps to */
    public int getValue() {
        return value;
    }
}
