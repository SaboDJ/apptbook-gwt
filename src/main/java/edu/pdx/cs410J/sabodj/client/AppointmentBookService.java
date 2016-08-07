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
   * Returns a list of all Appointment Book owners
   */
  public ArrayList<String> getOwners();

}
