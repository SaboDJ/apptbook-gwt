package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;



/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("appointments")
public interface AppointmentBookService extends RemoteService {

  /**
   * @param owner the name of the Appointment Book to return
   * Returns a String containing a PrettyPrint format of an Appointment Book
   */
  public String getAppointmentBook(String owner);

  /**
   * Adds an Appointment to the Appointment Books
   * @param owner the name of the Appointment Book to add the Appointment to
   * @param appt the Appointment being added
   * Returns a String containing a confirmation message
   */
  public String addAppointment(String owner, Appointment appt);

  /**
   * This method searches for all Appointments in a given range for a specific Appointment Book.
   * If the Appointment Book doesnt exist it will return an Appropriate message, otherwise it will
   * return a String containing all of the Appointment for a given owner within the range.
   *
   * @param owner the name of the Appointment Book to search
   * @param beginTime the start of the range (inclusive)
   * @param endTime the end of the range (inclusive)
   * Returns a PrettyPrint format of the Appointments within the range
   */
  public String getAppointmentsInRange(String owner, String beginTime, String endTime);

  /**
   * Returns a list of all Appointment Book owners
   */
  public ArrayList<String> getOwners();

  /**
   * Returns the View Appointment Book help information
   */
  public String getViewHelp();

  /**
   * Returns the Add Appointment help information
   */
  public String getAddHelp();

  /**
   * Returns the Search help information
   */
  public String getSearchHelp();

  /**
   * Returns the ReadMe
   */
  public String getReadMe();

}
