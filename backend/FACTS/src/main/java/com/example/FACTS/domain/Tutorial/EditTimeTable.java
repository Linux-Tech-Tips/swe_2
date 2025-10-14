package com.example.FACTS.domain.Tutorial;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rough service class, handles business logic for editing and managing Timetables
 * Will be updated in future to work functionally
 */
@Service
public class EditTimeTable {

    private final TimeSlotRepository timeSlotRepository;

    // Dependency Injection of repo, I don't think I did this right
    @Autowired
    public EditTimeTable(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    /**
     * Gets available Timeslots
     * @return A list of Timeslot objects marked as AVAILABLE.
     */
    public List<Timeslot> getAvailableTimeSlots() {
        return timeSlotRepository.findByAvailability(Availability.AVAILABLE);
    }

    /**
     * Gets all Timeslots
     * @return A list of all Timeslot objects.
     */
    public List<Timeslot> getAllTimeSlots() {
        // CrudRepository.findAll() returns an Iterable, convert to List
        return (List<Timeslot>) timeSlotRepository.findAll();
    }

    /**
     * PLACEHOLDER: the client sends full Timeslot object
     * and needs to find the matching one to update. This will be
     * updated in future to actually work
     *
     * @param timeslot The Timeslot to mark as UNAVAILABLE (matched by day and hour).
     * @return The updated Timeslot, or null if not found.
     */
    @Transactional
    public Timeslot setUnavailableTimeSlot(Timeslot timeslot) {
        // iterate to find a match, WILL NOT BE USED IN FINAL IMPLEMENTATION
        Optional<Timeslot> existingSlot = ((List<Timeslot>) timeSlotRepository.findAll()).stream().filter(t -> t.equals(timeslot)).findFirst();

        if (existingSlot.isPresent()) {
            Timeslot slotToUpdate = existingSlot.get();
            slotToUpdate.setAvailability(Availability.UNAVAILABLE);
            return timeSlotRepository.save(slotToUpdate);
        }
        return null;
    }

    /**
     * Sets Timeslot to AVAILABLE.
     * @param timeslot The Timeslot 
     * @return The updated Timeslot, or null if not found.
     */
    @Transactional
    public Timeslot setAvailableTimeSlot(Timeslot timeslot) {
        Optional<Timeslot> existingSlot = ((List<Timeslot>) timeSlotRepository.findAll()).stream().filter(t -> t.equals(timeslot)).findFirst();

        if (existingSlot.isPresent()) {
            Timeslot slotToUpdate = existingSlot.get();
            slotToUpdate.setAvailability(Availability.AVAILABLE);
            return timeSlotRepository.save(slotToUpdate);
        }
        return null;
    }

    /**
     * Initializes new TimeTable
     * @return A list of the newly saved Timeslots.
     */
    @Transactional
    public List<Timeslot> initializeTimeTable() {
        List<Timeslot> defaultSlots = TimeTable.generateTimeTable();
        return (List<Timeslot>) timeSlotRepository.saveAll(defaultSlots);
    }
}