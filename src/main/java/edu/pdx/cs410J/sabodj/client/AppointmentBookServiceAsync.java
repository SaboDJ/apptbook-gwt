package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;


/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {

  /**
   * Return the current date/time on the server
   */
 // void createAppointmentBook(int numberOfAppointments, AsyncCallback<AppointmentBook> async);

  void getAppointmentBook(String owner, AsyncCallback<ArrayList<AppointmentBook>> async);

  void addAppointment(String owner, Appointment appt, AsyncCallback<String> async);
}
