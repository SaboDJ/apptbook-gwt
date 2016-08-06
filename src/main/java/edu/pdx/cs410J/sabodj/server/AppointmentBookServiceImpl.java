package edu.pdx.cs410J.sabodj.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.sabodj.client.Appointment;
import edu.pdx.cs410J.sabodj.client.AppointmentBook;
import edu.pdx.cs410J.sabodj.client.AppointmentBookService;
import edu.pdx.cs410J.sabodj.client.PrettyPrinter;

import java.util.HashMap;
import java.util.Map;

/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{
  private Map<String, AppointmentBook> data = new HashMap<>();


  @Override
  public AppointmentBook createAppointmentBook(int numberOfAppointments) {
    AppointmentBook book = new AppointmentBook();
    for (int i = 0; i < numberOfAppointments; i++) {
      book.addAppointment(new Appointment());
    }
    return book;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

  @Override
  public String printAppointmentBook(String owner){
    String returnMessage = "";
    // If no owner print all books
    if(owner == null){
      if(data.size() != 0) {
        for (AppointmentBook book : this.data.values()) {
          returnMessage += PrettyPrinter.bookToString(book) + "\n";
        }
      }
      else {
        returnMessage = "There are no saved Appointment Books\n";
      }
    }
    else {
      AppointmentBook book = data.get(owner);
      // If owner doesn't exits, return an appropriate message
      if(book == null){
        returnMessage =  owner + " does not have an Appointment Book\n";
      }
      // return the owners appointment book printed pretty
      else {
        returnMessage =  PrettyPrinter.bookToString(book);
      }
    }

    return returnMessage;
  }

  @Override
  public String addAppointment(String owner, Appointment appt) {
    AppointmentBook book = data.get(owner);
    if(book == null){
      book = new AppointmentBook(owner, appt);
      data.put(owner, book);
      return owner + "'s appointment was added to " + owner + "'s new Appointment Book\n";
    }
    else{
      book.addAppointment(appt);
      return owner + "'s appointment was added to their Appointment Book\n";
    }
  }

}
