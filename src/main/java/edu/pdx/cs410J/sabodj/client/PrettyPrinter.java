package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Outputs the contents of an <code>AppointmentBook</code>, including all of its
 * <code>Appointment</code>'s in a nice, presentable format which can be outputted to a
 * file or to the screen.
 */
public class PrettyPrinter implements AppointmentBookDumper {

  /**
   * Constructor for the <code>PrettyPrinter</code>
   */
  PrettyPrinter(){
  }


  /**
   * Outputs the contents of the <code>AppointmentBook</code> in a pleasant, human readable format
   * to a file or to the screen if the <code>AppointmentBook</code> exists. If not, does nothing
   * and returns. If the constructor with a filename was used we will output to a file, otherwise
   * we will output to standard out.
   * @param book
   *        The <code>AppointmentBook</code> we are outputting
   */
  @Override
  public void dump(AbstractAppointmentBook book){
    // If there is nothing to output we just return
    if(book == null){
      return;
    }
    String output = bookToString((AppointmentBook) book);
    System.out.println(output);
  }

  /**
   * Converts an appointment book to a string for easy output
   * @param book
   *        The Appointment Book we are converting
   * Returns a nicely formatted String containing all contents of the Appointment Book
   */
  public static String bookToString(AppointmentBook book) {

    // Build the output
    StringBuffer buffer = new StringBuffer();
    ArrayList<Appointment> appts = (ArrayList<Appointment>)book.getAppointments();
    buffer.append("Hello " + book.getOwnerName() + ", you have " +  appts.size() + " appointments:\n");
    // Set up a nice date output

    // Loop over all of the appointments in the book
    for(Appointment appt : appts) {
      buffer.append(appt.getDescription() + " lasting " + appt.getDurationInMinutes() +  " minutes. It starts at ");
      buffer.append(dateToString(appt.getBeginTime()) + " and ends at " + dateToString(appt.getEndTime()) + "\n");
    }
    return buffer.toString();
  }
    private static String dateToString(Date date){
        String pattern = "h:mm a EEE MMM d yyyy";
        return  DateTimeFormat.getFormat(pattern).format(date);
    }

}
