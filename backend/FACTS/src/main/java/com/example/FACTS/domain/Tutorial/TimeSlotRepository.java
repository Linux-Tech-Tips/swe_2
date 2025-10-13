/**
 * Used for allowing Timeslots to be saved into a database.
 */
package com.example.FACTS.domain.Tutorial;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TimeSlotRepository extends CrudRepository<Timeslot, Long> {
    List<Timeslot> findByAvailability(Availability a);

}