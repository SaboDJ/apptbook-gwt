package edu.pdx.cs410J.sabodj.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  @VisibleForTesting
  private  AppointmentBookServiceAsync async;

  // Main
  Button      button;
  Button      helpButton;
  Button      viewAppointmenBookButton;
  Button      addAppointmentButton;
  Button      searchAppointmentButton;

  DockPanel readMePanel;

  // View Appointments
  HorizontalPanel viewApptsPanel;
  ListBox     ownersList;
  TextArea     viewApptTextBox;

  // Add Appointment
  VerticalPanel   addApptPanel;
  TextBox     ownerBox;
  TextBox     descriptionBox;
  TimeFields  begintTimeFields;
  TimeFields  endTimeFields;
  Button      addApptButton;

  // Search Appointments
  VerticalPanel   searchPanel;
  Button      searchButton;

  TextBox     textBox;
  TextArea    mainTextArea;

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;

    addWidgets();
  }

  private void addWidgets() {
    async = GWT.create(AppointmentBookService.class);

    helpButton = new Button("Help");
    helpButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
        searchButton.setVisible(false);
        searchPanel.setVisible(false);
        readMePanel.setVisible(true);
        viewApptsPanel.setVisible(false);
        printReadme();
      }
    });

    // View Appointment
    viewApptsPanel = new HorizontalPanel();
    viewApptTextBox = new TextArea();

    viewAppointmenBookButton = new Button("View Appointment Book");
    viewAppointmenBookButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
        searchButton.setVisible(false);
        searchPanel.setVisible(false);
        readMePanel.setVisible(false);

        setOwnersList();
        viewApptsPanel.setVisible(true);
        viewApptTextBox.setVisible(false);

       // viewAppointmentBook();
      }
    });
    ownersList = new ListBox();
    setOwnersList();
    ownersList.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent changeEvent) {
        String selected = ownersList.getSelectedItemText();
        if(selected.equals("Select Owner")){
          viewApptTextBox.setText("");
          viewApptTextBox.setVisible(false);
        }
        // If it equals all, print all Appointment Books
        else if(selected.equals("All")){
          viewAppointmentBook(null);
        }
        // Else, print the selected Appointment Book
        else{
          viewAppointmentBook(selected);
        }
      }
    });

    addAppointmentButton = new Button("Add Appointment");
    addAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        readMePanel.setVisible(false);
        searchButton.setVisible(false);
        searchPanel.setVisible(false);
        addApptPanel.setVisible(true);
        addApptButton.setVisible(true);
        viewApptsPanel.setVisible(false);
      }
    });

    searchAppointmentButton = new Button("Search");
    searchAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        readMePanel.setVisible(false);
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
        searchPanel.setVisible(true);
        searchButton.setVisible(true);
        viewApptsPanel.setVisible(false);
       // Call the search method
      }
    });


    // Add Appt Items
    this.addApptPanel = new VerticalPanel();
    addApptButton = new Button("Add");
    addApptButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (addAppointment()) {
          addApptButton.setVisible(false);
          addApptPanel.setVisible(false);

          // Print out the confirmation message
          StringBuffer output = new StringBuffer();
          output.append(getOwner());
          output.append("'s new appointment was successfully added.");
          mainTextArea.setCharacterWidth(80);
          mainTextArea.setVisibleLines(1);
          mainTextArea.setText(output.toString());
          readMePanel.setVisible(true);

          // Clear the text blocks
          ownerBox.setText("");
          descriptionBox.setText("");
          begintTimeFields.reset();
          endTimeFields.reset();

        }
      }
    });
    this.ownerBox = new TextBox();
    this.descriptionBox = new TextBox();
    this.begintTimeFields = new TimeFields();
    this.endTimeFields = new TimeFields();

    // Search Items
    searchPanel = new VerticalPanel();
    searchButton = new Button("Search");
    searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchPanel.setVisible(false);
        searchButton.setVisible(false);
        //searchAppointmentToBook();
      }
    });

    this.mainTextArea = new TextArea();

    this.textBox = new TextBox();

    this.readMePanel = new DockPanel();
  }

  /**
   * This method prints out the readme to the main text box
   */
  private void printReadme() {
    this.mainTextArea.setCharacterWidth(80);
    this.mainTextArea.setVisibleLines(20);
    this.mainTextArea.setText("Name: DJ Sabo\nProject 5: Rich Internet Application For Appointment Book\n");

  }

  private void viewAppointments() {
    this.mainTextArea.setCharacterWidth(120);
    this.mainTextArea.setVisibleLines(40);
    setOwnersList();
    this.mainTextArea.setText("Need to print out all appointments");
  }

  /**
   * Returns a ListBox containing the names of all the owners stored on the server
   */
  public void setOwnersList() {
    ownersList.clear();
    ownersList.addItem("Select Owner");
    ownersList.addItem("All");
    this.async.getOwners( new AsyncCallback<ArrayList<String>>() {
      @Override
      public void onSuccess(ArrayList<String> owners) {
        for (String owner : owners) {
          ownersList.addItem(owner);
        }
      }
      @Override
      public void onFailure(Throwable throwable) {
        alerter.alert("Failed to get list of owners");
      }
    });
  }


  private boolean addAppointment(){
    String owner = getOwner();
    String description = getDescription();
    if(owner == null || owner.equals("")){
      alerter.alert("Owner cannot be empty");
      return false;
    }
    if(description == null || description.equals("")){
      alerter.alert("Description cannot be empty");
      return false;
    }

      String beginTime = begintTimeFields.getDate();
      String endTime = endTimeFields.getDate();

    try {
      Appointment appt = new Appointment(description, beginTime, endTime);
   //   AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
      this.async.addAppointment(owner, appt, new AsyncCallback<String>() {

        @Override
        public void onSuccess(String result) {
          mainTextArea.setCharacterWidth(100);
          mainTextArea.setVisibleLines(1);
          mainTextArea.setText(result);
        }

        @Override
        public void onFailure(Throwable ex) {
          alert(ex);
        }
      });


    } catch (ParseException pe){
      alerter.alert("Error: " + pe.getMessage());
      return false;
    }

    return true;
  }


//  private void createAppointments() {
//  //  AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
//    int numberOfAppointments = getNumberOfAppointments();
//    async.createAppointmentBook(numberOfAppointments, new AsyncCallback<AppointmentBook>() {
//
//      @Override
//      public void onSuccess(AppointmentBook airline) {
//        displayInAlertDialog(airline);
//      }
//
//      @Override
//      public void onFailure(Throwable ex) {
//        alert(ex);
//      }
//    });
//  }

  public void viewAppointmentBook(String owner){
    this.async.getAppointmentBook(owner, new AsyncCallback<ArrayList<AppointmentBook>>() {

      @Override
      public void onSuccess(ArrayList<AppointmentBook> list) {
        viewApptTextBox.setVisible(true);
        viewApptTextBox.setCharacterWidth(100);
        viewApptTextBox.setVisibleLines(1);
//         if(owner != null || !owner.equals("")) {
//           viewApptTextBox.setText(owner + " does not have any appointments");
//        }

        if(list.size() == 0){
           viewApptTextBox.setCharacterWidth(80);
           viewApptTextBox.setVisibleLines(1);
           viewApptTextBox.setText("There are no saved Appointment Books's");
        }
        else {
          StringBuilder output = new StringBuilder();
          for(int i = 0; i < list.size(); i++){
           // output.append( PrettyPrinter.bookToString(list.get(i)));
            output.append(list.get(i).toString());
            output.append("\n");
          }
           viewApptTextBox.setVisibleLines(40);
           viewApptTextBox.setText(output.toString());
        }
      }

      @Override
      public void onFailure(Throwable ex) {
        alert(ex);
      }
    });
  }

  private int getNumberOfAppointments() {
    String number = this.textBox.getText();

    return Integer.parseInt(number);
  }

  /**
   * Returns the string contained in the ownerBox textbox
   */
  private String getOwner(){
    return this.ownerBox.getText();
  }

  /**
   *
   * Returns the string contained in the descriptionBox textbox
   */
  private String getDescription(){
    return this.descriptionBox.getText();
  }

  private void displayInAlertDialog(AppointmentBook airline) {
    StringBuilder sb = new StringBuilder(airline.toString());
    sb.append("\n");

    Collection<Appointment> flights = airline.getAppointments();
    for (Appointment flight : flights) {
      sb.append(flight);
      sb.append("\n");
    }
    alerter.alert(sb.toString());
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();
    rootPanel.add(helpButton);
    rootPanel.add(viewAppointmenBookButton);
    rootPanel.add(addAppointmentButton);
    rootPanel.add(searchAppointmentButton);
  //  rootPanel.add(button);

//    DockPanel panel = new DockPanel();
//    panel = new DockPanel();
//    panel.add(new Label("Number of appointments"), DockPanel.WEST);
//    panel.add(textBox, DockPanel.CENTER);

//    rootPanel.add(panel);

    // Text panel for printing readme and appointment book
    readMePanel.setVisible(false);
    readMePanel.add(this.mainTextArea, DockPanel.CENTER);
    rootPanel.add(readMePanel);

    // Setup View Appointments Panel
    viewApptsPanel.setVisible(false);
    viewApptsPanel.add(ownersList);
    viewApptsPanel.add(viewApptTextBox);
    rootPanel.add(viewApptsPanel);

    // Setup Add Appointment panel
    addApptPanel.setVisible(false);
    HorizontalPanel apptPanel1 = new HorizontalPanel();
    apptPanel1.add(new Label("Owner"));
    apptPanel1.add(this.ownerBox);
    apptPanel1.add(new Label("Description"));
    apptPanel1.add(this.descriptionBox);
    HorizontalPanel apptPanel2 = new HorizontalPanel();
    apptPanel2.add(new Label("Begin Date"));
    apptPanel2.add(this.begintTimeFields.month);
    apptPanel2.add(new Label("/"));
    apptPanel2.add(this.begintTimeFields.day);
    apptPanel2.add(new Label("/"));
    apptPanel2.add(this.begintTimeFields.year);
    apptPanel2.add(new Label("...Begin Time"));
    apptPanel2.add(this.begintTimeFields.hour);
    apptPanel2.add(new Label(":"));
    apptPanel2.add(this.begintTimeFields.minute);
    apptPanel2.add(this.begintTimeFields.ampm);
    HorizontalPanel apptPanel3 = new HorizontalPanel();
    apptPanel3.add(new Label("...End Date"));
    apptPanel3.add(this.endTimeFields.month);
    apptPanel3.add(new Label("/"));
    apptPanel3.add(this.endTimeFields.day);
    apptPanel3.add(new Label("/"));
    apptPanel3.add(this.endTimeFields.year);
    apptPanel3.add(new Label("......End Time"));
    apptPanel3.add(this.endTimeFields.hour);
    apptPanel3.add(new Label(":"));
    apptPanel3.add(this.endTimeFields.minute);
    apptPanel3.add(this.endTimeFields.ampm);
    addApptPanel.add(apptPanel1);
    addApptPanel.add(apptPanel2);
    addApptPanel.add(apptPanel3);
    addApptPanel.add(addApptButton);
    rootPanel.add(this.addApptPanel);


    // Setup Search  panel
//    searchPanel.setVisible(false);
//    HorizontalPanel searchPan1 = new HorizontalPanel();
//    searchPan1.add(new Label("Owner"));
//    searchPan1.add(this.ownerBox);
//    searchPan1.add(searchPan1);
//    searchPan1.add(apptPanel2);
//    rootPanel.add(searchPanel);



    searchButton.setVisible(false);
    rootPanel.add(searchButton);


  }



  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}
