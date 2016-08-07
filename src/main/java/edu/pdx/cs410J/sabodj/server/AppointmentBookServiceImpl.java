package edu.pdx.cs410J.sabodj.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.sabodj.client.Appointment;
import edu.pdx.cs410J.sabodj.client.AppointmentBook;
import edu.pdx.cs410J.sabodj.client.AppointmentBookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{
  private Map<String, AppointmentBook> data = new HashMap<>();

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

  /**
   * This method will convert a single or all appointment books to a String in the
   * PrettyPrint format.
   *
   * @param owner the name of the Appointment Book to return
   * Returns a String containing an Appointment Book in the PrettyPrint format,
   *              or all Appointment Books Pretty Printed if null was passed in
   */
  @Override
  public String getAppointmentBook(String owner) {
    String returnMessage = "";
    // If no owner print all books
    if(data == null || data.size() == 0){
      returnMessage = "There are no saved Appointment Books\n";
    }
    // If no owner passed in, print all Appointment Books
    else if(owner == null){
      for (AppointmentBook book : this.data.values()) {
        returnMessage += new PrettyPrinter().bookToString(book) + "\n";
      }
    }
    // If an owner was passed in, print their book if it exists
    else {
      AppointmentBook book = data.get(owner);
      // If owner doesn't exits, return an appropriate message
      if(book == null){
        returnMessage =  owner + " does not have an Appointment Book\n";
      }
      // return the owners appointment book printed pretty
      else {
        returnMessage = new PrettyPrinter().bookToString(book);
      }
    }
    return returnMessage;
  }

   /**
    * Adds an Appointment to the owners Appointment Book, if the Book doesn't exists it
    * creates one.
    *
    * @param owner the name of the Appointment Book to add the Appointment to
    * @param appt the Appointment to be added
    * Returns a String containing a confirmation message
    */
   @Override
   public String addAppointment(String owner, Appointment appt) {
       AppointmentBook book = data.get(owner);
       if(book == null) {
           book = new AppointmentBook(owner, appt);
           data.put(owner, book);
           return owner + "'s appointment was added to " + owner + "'s new Appointment Book\n";

       } else {
           book.addAppointment(appt);
           return owner + "'s appointment was added to their Appointment Book\n";
       }
   }

    /**
     * Returns a sorted ArrayList containing all fo the Appointment Book owner's names
     */
    @Override
    public ArrayList<String> getOwners() {
        ArrayList<String> list = new ArrayList<>();
        for (String owner : data.keySet() ){
            list.add(owner);
        }
        Collections.sort(list);
        return list;
    }
}
