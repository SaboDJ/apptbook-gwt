package edu.pdx.cs410J.sabodj.client;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

import java.text.ParseException;
import java.util.*;
import java.util.Collections;

/**
 * This class is represents a <code>AppointmentBook</code> and extends AbstractAppointmentBook.
 * The <code>AppointmentBook</code> is a collection of <code>Appointment</code>'s and a owner
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
    private String owner;
    private List<Appointment> appts;

    /**
     * Creates a new <code>AppointmentBook</code>
     *
     * @param owner
     *        The owner of the <code>AppointmentBook</code>
     */
    public AppointmentBook(String owner) {
        this.owner = owner;
        this.appts = new ArrayList<>();
    }

    /**
     * Creates a new <code>AppointmentBook</code>
     *
     * @param owner
     *        The owner of the <code>AppointmentBook</code>
     * @param appt
     *        The <code>Appointment</code> to add to the <code>AppointmentBook</code>
     */
    public AppointmentBook(String owner, Appointment appt) {
        this.owner = owner;
        this.appts = new ArrayList<>();
        if(appt != null) {
            this.appts.add(appt);
        }
    }

    /**
     * Public no-argument constructor which is required by serializable
     * This should never be called but if it is it will create an AppointmentBook with the
     * owner name "default owner" and an empty ArrayList to store the Appointments
     */
    public AppointmentBook() {
        this.owner = "default owner";
        this.appts = new ArrayList<>();
    }

    /**
     * Returns a <code>String</code> that contains the <code>AppointmentBook</code> owner's name
     */
    @Override
    public String getOwnerName() {
        return owner;
    }

    /**
     * Returns a sorted <code>Collection</code> that contains the <code>AppointmentBook</code> <code>Appointment</code>'s
     */
    @Override
    public Collection<Appointment> getAppointments() {
        Collections.sort(appts);
        return appts;
    }

    /**
     * Adds a <code>Appointment</code> to the <code>AppointmentBook</code>
     */
    @Override
    public void addAppointment(Appointment appt) {
        appts.add(appt);
    }

    /**
     * This method filters our appointment book by the given start and end dates/times a
     *
     * @param start is the time the appointments must start after
     * @param end is the time the appointments but end before
     * Returns an ArrayList contaning all of the appointments in the given range
     * @throws ParseException
     *        Throws an exception if there was an error parsing either of the dates
     */
    public ArrayList<Appointment> getApptsInRange(String start, String end) throws ParseException, IllegalStateException {
        ArrayList<Appointment> list = new ArrayList<>();
        Date startTime = null;
        Date endingTime = null;

        try {
            startTime = Appointment.convertStringToDate(start);
            endingTime = Appointment.convertStringToDate(end);

        } catch (ParseException pe) {
            throw new ParseException("Invalid Date", pe.getErrorOffset());
        }

        // If the start time is not less then the end time throw and exception
        if(startTime.compareTo(endingTime) > 0){
            throw new IllegalStateException("Start time must be before ending time");
        }



        // Loop over all of our appointments and add ones inside of the range to the list
        for(int i = 0; i < appts.size(); i++){
            Appointment appt = appts.get(i);
            if(appt.getBeginTime().compareTo(startTime) >= 0 && appt.getEndTime().compareTo(endingTime) <= 0 ) {
                list.add(appt);
            }
        }
        // Sort the appointments
        Collections.sort(list);
        return list;
    }

    /**
     * Returns a <code>int</code> that contains number of <code>Appointment</code>'s in the <code>AppointmentBook</code>
     */
    int getAppointmentCount() {
        return this.appts.size();
    }
}


//public class AppointmentBook extends AbstractAppointmentBook<Appointment>
//{
//
//    private Collection<Appointment> appts = new ArrayList<>();
//
//    @Override
//    public String getOwnerName()
//    {
//        return "My Owner";
//    }
//
//    @Override
//    public Collection<Appointment> getAppointments()
//    {
//        return this.appts;
//    }
//
//    @Override
//    public void addAppointment( Appointment appt )
//    {
//        this.appts.add(appt);
//    }
//}
