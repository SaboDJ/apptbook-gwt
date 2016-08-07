package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import edu.pdx.cs410J.AbstractAppointment;

import java.text.ParseException;
import java.util.Date;

/**
 * This class is represents a single <code>Appointment</code> and extends AbstractAppointment.
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment>{
    private String description;
    private Date beginTime;
    private Date endTime;

    /**
     * Creates a new <code>Appointment</code>
     *
     * @param description
     *        The description of the <code>Appointment</code>
     * @param beginTimeString
     *        The start time of the <code>Appointment</code>
     * @param endTimeString
     *        The ending time of the <code>Appointment</code>
     */
    public Appointment(String description, String beginTimeString, String endTimeString) throws ParseException {
        this.description = description;
        this.beginTime = convertStringToDate(beginTimeString);
        this.endTime = convertStringToDate(endTimeString);
        if(this.beginTime.compareTo(this.endTime) > 0){
            throw new ParseException("End time must be after Begin time", 0);
        }
    }

    /**
     * Public no-argument constructor which is required by serializable
     * This should never be called but if it is it will create an Appointment with the
     * description "default appointment" and the beginTime and endTime both being set to
     * the current time
     */
    public Appointment() {
        this.description = "An Appointment";
        this.beginTime = new Date();
        this.endTime = new Date();
    }

    /**
     * This method will take a <code>String</code> formatted M/D/YYYY h:m and convert it into
     * a <code>LocalDateTime</code> object. It also validates that the date passed in is a real date
     * and accounts for leap years.
     *
     * @param date
     *        The <code>String</code> that will be converted to a <code>LocalDateTime</code>
     * Returns the converted date if successful, null if unsuccessful
     */
    static Date convertStringToDate(String date) throws ParseException {
        Date aDate;
        try { // If the date is invalid it will throw an exception
            String pattern = "M/d/yyyy h:m a";
            aDate = DateTimeFormat.getFormat(pattern).parseStrict(date);
        }
        // If it wasn't able to set the date because of invalid format or a bad date, throws an exception
        catch (IllegalArgumentException iae) {
            throw new ParseException(date + " is not a valid date/time", 0);
        }
        catch (NullPointerException np) {
            throw new ParseException("Date cannot be null", 0);
        }

        // If there were no problems, returns date object;
        return aDate;
    }

    /**
     * Converts a Date into a string formatted nicely
     * @param date the date to convert
     * Returns a string formatted in DATE_SHORT form as a String
     */
    private String formatDate(Date date) {
        return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(date);
    }

    /**
     * Returns a <code>String</code> that contains the starting time formatted: M/D/YY h:m a
     */
    @Override
    public String getBeginTimeString() {
        return formatDate(beginTime);
    }

    /**
     * Returns a <code>String</code> that contains the ending time formatted: M/D/YY h:m a
     */
    @Override
    public String getEndTimeString() {
       return formatDate(endTime);
    }

    /**
     * Returns a <code>String</code> that contains this <code>Appointment</code>'s description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns a <code>Date</code> that contains this <code>Appointment</code>'s begin time
     */
    @Override
    public Date getBeginTime(){
        return beginTime;
    }

    /**
     * Returns a <code>Date</code> that contains this <code>Appointment</code>'s end time
     */
    @Override
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method overrides compareTo to determine if the current appointment is less than, equal to, or greater than
     * the passed in appointment. It will first compare the start times, if equal it will compare end time, if end times
     * are equal it will compare their descriptions lexicographically.
     * @param appt
     *        The appointment this appointment will be compared to.
     * Returns -1 if this appointment is less than the argument, 0 if both have equal start times, end times, and
     *        descriptions, and 1 if this appointment is greater then the passed in argument.
     */
    @Override
    public int compareTo(Appointment appt) {
        // Compare the begin times. if they are not equal we will return the results
        int result = this.beginTime.compareTo(appt.getBeginTime());
        if (result != 0){
            return result;
        }
        // Compare the end times. if they are not equal we will return the results
        result = this.endTime.compareTo(appt.getEndTime());
        if (result != 0) {
            return result;
        }
        // if the start and end times are equal we will return the comparison of the descriptions even if they are equal
        return this.description.compareTo(appt.getDescription());
    }

    /**
     * Returns the length of the <code>Appointment</code> in minutes
     */
    public long getDurationInMinutes(){
        long duration = this.endTime.getTime() - this.beginTime.getTime();
        return duration/60000;
    }
}

