/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.timetable;

import java.util.ArrayList;
import java.util.List;

/** Data Value Object representing user availability in a timetable  */
public class Timetable {

    /** The availability matrix signifying which timeslots are available and which are busy */
    private List<List<Availability>> availability;
    /** The number of rows in the timetable */
    private int columns;
    /** The number of columns in the timetable */
    private int rows;

    /** The default number of columns is 5, representing 5 workdays in a week */
    public static final int DEFAULT_COLUMNS = 5;
    /** The default number of rows is 8, representing 8 hours in a workday */
    public static final int DEFAULT_ROWS = 8;
    /** The default setting is that all slots are available */
    public static final Availability DEFAULT_AVAILABILITY = Availability.AVAILABLE;

    /** Default constructor, initializes an empty timetable */
    public Timetable() {
        this(DEFAULT_COLUMNS, DEFAULT_ROWS, DEFAULT_AVAILABILITY);
    }

    /** Initializes a Timetable with the specific parameters */
    public Timetable(int columns, int rows, Availability defaultValue) {
        this.columns = columns;
        this.rows = rows;
        /* Populate the availability matrix */
        this.availability = new ArrayList<>(rows);
        for(int i = 0; i < rows; ++i) {
            List<Availability> row = new ArrayList<>(columns);
            for(int j = 0; j < columns; ++j) {
                row.add(defaultValue);
            }
            this.availability.add(row);
        }
    }

    /** Sets the availability of a given time slot */
    public void setAvailability(int column, int row, Availability available) {
        availability.get(row).set(column, available);
    }

    /** Gets the availability of a given time slot */
    public Availability getAvailability(int column, int row) {
        return availability.get(row).get(column);
    }

    /** Gets the flat (array) representation of the availability matrix */
    public List<Availability> getMatrix() {
        List<Availability> result = new ArrayList<>(rows * columns);
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < columns; ++j) {
                result.add(availability.get(i).get(j));
            }
        }
        return result;
    }

}
