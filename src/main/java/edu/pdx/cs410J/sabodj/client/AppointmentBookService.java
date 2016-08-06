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
   * Returns the current date and time on the server
   */
 // public AppointmentBook createAppointmentBook(int numberOfAppointments);

  public ArrayList<AppointmentBook> getAppointmentBook(String owner);

  public String addAppointment(String owner, Appointment appt);

  public ArrayList<String> getOwners();

}
