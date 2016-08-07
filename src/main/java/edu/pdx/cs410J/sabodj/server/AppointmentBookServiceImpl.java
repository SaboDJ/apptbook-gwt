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


//  @Override
//  public AppointmentBook createAppointmentBook(int numberOfAppointments) {
//    AppointmentBook book = new AppointmentBook();
//    for (int i = 0; i < numberOfAppointments; i++) {
//      book.addAppointment(new Appointment());
//    }
//    return book;
//  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

  @Override
  public ArrayList<AppointmentBook> getAppointmentBook(String owner) {
    ArrayList<AppointmentBook> list = new ArrayList<>();
    // if we dont have any save Appointment Books, return empty list
    if (data.size() == 0)
      return list;
    // if no owner was passed in, return all Appointment Books
    if (owner == null) {
      for (AppointmentBook book : this.data.values()) {
        list.add(book);
      }
      return list;
    }
    // else, see if the owner has an Appointment Book
    AppointmentBook book =  data.get(owner);
    if(book != null){
      list.add(book);
  }
    return list;

//    String returnMessage = "";
//    // If no owner print all books
//    if(data == null || data.size() == 0){
//      returnMessage = "There are no saved Appointment Books\n";
//    }
//    // If no owenr passed in, print all Appointment Books
//    else if(owner == null){
//      for (AppointmentBook book : this.data.values()) {
//        returnMessage += new PrettyPrinter().bookToString(book) + "\n";
//        //returnMessage += book.toString() + "\n";
//      }
//    }
//    // If an owner was passed in, print their book if it exists
//    else {
//      AppointmentBook book = data.get(owner);
//      // If owner doesn't exits, return an appropriate message
//      if(book == null){
//        returnMessage =  owner + " does not have an Appointment Book\n";
//      }
//      // return the owners appointment book printed pretty
//      else {
//        returnMessage = book.toString();
//      }
//    }
//
//    return returnMessage;
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
