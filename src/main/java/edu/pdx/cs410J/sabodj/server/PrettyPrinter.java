package edu.pdx.cs410J.sabodj.server;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;
import edu.pdx.cs410J.sabodj.client.Appointment;
import edu.pdx.cs410J.sabodj.client.AppointmentBook;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Outputs the contents of an <code>AppointmentBook</code>, including all of its
 * <code>Appointment</code>'s in a nice, presentable format
 */
public class PrettyPrinter implements AppointmentBookDumper {

  /**
   * Constructor for the <code>PrettyPrinter</code>
   */
  PrettyPrinter(){
  }

  /**
   * Outputs the contents of the <code>AppointmentBook</code> in a pleasant, human readable format
   * to the screen if the <code>AppointmentBook</code> exists, if not it does nothing and returns.
   *
   * @param book
   *        The <code>AppointmentBook</code> we are outputting
   * @throws IOException
   *          Should not throw any exception because it no longer involves a file.
   */
  @Override
  public void dump(AbstractAppointmentBook book) throws IOException {
    // If there is nothing to output we just return
    if(book == null){
      return;
    } else {
      String output = bookToString((AppointmentBook) book);
      System.out.println(output);
    }
  }

  /**
   * Converts an appointment book to a string for easy output
   * @param book
   *        The Appointment Book we are converting
   * Returns a nicely formatted String containing all contents of the Appointment Book
   */
  public String bookToString(AppointmentBook book) {

    // Build the output
    StringBuffer buffer = new StringBuffer();
    ArrayList<Appointment> appts = (ArrayList<Appointment>)book.getAppointments();
    buffer.append("Hello " + book.getOwnerName() + ", you have " +  appts.size() + " appointments:\n");

    // Set up a nice date output
    DateFormat formatter= new SimpleDateFormat("h:mm a EEE MMM d yyyy");

    // Loop over all of the appointments in the book
    for(Appointment appt : appts) {
      buffer.append(appt.getDescription() + " lasting " + appt.getDurationInMinutes() +  " minutes. It starts at ");
      buffer.append(formatter.format(appt.getBeginTime()) + " and ends at " + formatter.format(appt.getEndTime()) + "\n");
    }

    return buffer.toString();
  }

}
