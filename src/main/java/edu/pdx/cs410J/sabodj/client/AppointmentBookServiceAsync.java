package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {

  /**
   * Return the current date/time on the server
   */
 // void createAppointmentBook(int numberOfAppointments, AsyncCallback<AppointmentBook> async);

  void printAppointmentBook(String owner, AsyncCallback<String> async);

  void addAppointment(String owner, Appointment appt, AsyncCallback<String> async);
}
