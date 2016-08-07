package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;


/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {

  /**
   * @param owner the name of the Appointment Book to return
   * Returns a String containing a PrettyPrint format of an Appointment Book
   */
  void getAppointmentBook(String owner, AsyncCallback<String> async);

  /**
   * Adds an Appointment to the Appointment Books
   * @param owner the name of the Appointment Book to add the Appointment to
   * @param appt the Appointment being added
   * Returns a String containing a confirmation message
   */
  void addAppointment(String owner, Appointment appt, AsyncCallback<String> async);

  /**
   * This method searches for all Appointments in a given range for a specific Appointment Book.
   * If the Appointment Book doesnt exist it will return an Appropriate message, otherwise it will
   * return a String containing all of the Appointment for a given owner within the range.
   *  @param owner the name of the Appointment Book to search
   * @param beginTime the start of the range (inclusive)
   * @param endTime the end of the range (inclusive)
   */
  void getAppointmentsInRange(String owner, Date beginTime, Date endTime, AsyncCallback<String> async);

  /**
   * Returns a list of all Appointment Book owners
   */
  void getOwners(AsyncCallback<ArrayList<String>> async);

  /**
   * Returns the View Appointment Book help information
   */
  void getViewHelp(AsyncCallback<String> async);

  /**
   * Returns the Add Appointment help information
   */
  void getAddHelp(AsyncCallback<String> async);

  /**
   * Returns the Search help information
   */
  void getSearchHelp(AsyncCallback<String> async);

  /**
   * Returns the ReadMe
   */
  void getReadMe(AsyncCallback<String> async);
}
