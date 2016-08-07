package edu.pdx.cs410J.sabodj.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.sabodj.client.Appointment;
import edu.pdx.cs410J.sabodj.client.AppointmentBook;
import edu.pdx.cs410J.sabodj.client.AppointmentBookService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
     * This method searches for all Appointments in a given range for a specific Appointment Book.
     * If the Appointment Book doesnt exist it will return an Appropriate message, otherwise it will
     * return a String containing all of the Appointment for a given owner within the range.
     *
     * @param owner the name of the Appointment Book to search
     * @param beginTimeString the start of the range (inclusive)
     * @param endTimeString the end of the range (inclusive)
     * Returns a PrettyPrint format of the Appointments within the range
     */
    public String getAppointmentsInRange(String owner, String beginTimeString, String endTimeString){
        AppointmentBook book = data.get(owner);
        if(book == null){
            return owner + " does not have an Appointment Book";
        }
        else{
            try{
                Date beginTime = convertStringToDate(beginTimeString);
                Date endTime = convertStringToDate(endTimeString);
                ArrayList<Appointment> list = book.getApptsInRange(beginTime, endTime);
                if(list.size() == 0){
                    DateFormat f = new SimpleDateFormat("h:mm a EEE MMM d yyyy");
                    return owner + " does not have any appointments between " + f.format(beginTime) + " and " + f.format(endTime);
                }
                else {
                    AppointmentBook newBook = new AppointmentBook(owner);
                    for(Appointment appt : list){
                        newBook.addAppointment(appt);
                    }
                    return new PrettyPrinter().bookToString(newBook);
                }
            } catch (Exception e){
                return e.getMessage();
            }
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

    /**
     * Returns the View Appointment Book help information
     */
    @Override
    public String getViewHelp() {
        return "View Appointment Book Help:\n\n" +
                "The View Appointment Book page will let you view a single appointment book or all of the\n" +
                "appointment books stored on the site. The dropdown menu on the left of the page will let\n" +
                "you select a particular owner or all. When you click on your choice a text window will\n" +
                "appear containing the related appointments.\n";
    }

    /**
     * Returns the Add Appointment help information
     */
    @Override
    public String getAddHelp() {
        return "Add Appointment Help:\n\n" +
                "The Add Appointment page will assist you in adding appointments to your appointment book.\n" +
                "You can have multiple appointment books as long as they have different owners. If you add\n" +
                "an appointment with an owner that does not yet exist, a new appointment book will be created\n" +
                "for you.\n\n" +
                "Requirements: \n" +
                "   Owner: the name of the appointment book\n" +
                "   Description: a description of the appointment\n" +
                "   Begin Time: when the appointment will start\n" +
                "   End Time: when the appointment will end\n";
    }

    /**
     * Returns the Search help information
     */
    @Override
    public String getSearchHelp() {
        return "Search Help:\n\n" +
                "The search option will search for though an Appointment Book for all appointments between\n" +
                "two dates. If the owner doesnt have an appointment book or the owner doesnt have any\n" +
                "appointments between the give dates an appropiate message will be displayed. \n\n" +
                "Requirements: \n" +
                "   Owner: the name of the appointment book to search\n" +
                "   Begin Time: the time equal or less than the appointments to search for\n" +
                "   End Time: the time equal to or greater than the appointments to search for.\n";
    }

    /**
     * Returns the ReadMe
     */
    @Override
    public String getReadMe() {
        return "Name: DJ Sabo\n" +
                "Project 5: Rich Internet Application For Appointment Book\n\n" +
                "   This program is designed to use our previously designed Appointment, AppointmentBook, and\n" +
                "PrettyPrint classes and implement a rich internet application using the Google Web Toolkit.This\n" +
                "application contains a help menu, the ability to view saved appointment books, the ability to\n" +
                "create new appointment books, the ability to add appointments, and the ability to search for\n" +
                "all appointments for in an appointment book based on a date range. The appointment books are\n" +
                "saved on the server and accessed through the Google Web Toolkit's mechanism for communicating.\n" +
                "The help menu contains instructions on how to do each of these operations. If there are any \n" +
                "errors, such as an invalid input, a pop up window will appear with the given error.";
    }

    /**
     * This method will take a <code>String</code> formatted M/D/YYYY h:m and convert it into
     * a <code>LocalDateTime</code> object. It also validates that the date passed in is a real date
     * and accounts for leap years.
     *
     * @param date
     *        The <code>String</code> that will be converted to a <code>LocalDateTime</code>
     * Returns the converted date if successful, null if unsuccessful
     */
    static Date convertStringToDate(String date) throws ParseException {
        Date aDate;
        try { // If the date is invalid it will throw an exception
            DateFormat fm= new SimpleDateFormat("M/d/yyyy h:m a");
            fm.setLenient(false);
            aDate = fm.parse(date);
        }
        // If it wasn't able to set the date because of invalid format or a bad date, throws an exception
        catch (ParseException pe) {
            throw new ParseException(date + " is not a valid date/time", pe.getErrorOffset());
        }

        catch (NullPointerException np) {
            throw new ParseException("Date cannot be null", 0);
        }

        // Check to make sure the date has 4 digits
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(aDate);
        int year = calendar.get(Calendar.YEAR);
        if (year < 1000){
            throw new ParseException("Year must be four digits", 0);
        }
        // If there were no problems, returns date object;
        return aDate;
    }
}
