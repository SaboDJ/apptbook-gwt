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
  Button      helpButton;
  Button      viewAppointmenBookButton;
  Button      addAppointmentButton;
  Button      searchAppointmentButton;

  // Help Menu
  HorizontalPanel helpPanel;
  VerticalPanel   helpMenu;
  Button      readmeHelp;
  Button      viewApptHelp;
  Button      addApptHelp;
  Button      searchHelp;
  TextArea    helpTextArea;
  DockPanel   mainTextPanel;
  TextArea    mainTextArea;

  // View Appointments
  HorizontalPanel viewApptsPanel;
  ListBox     ownersList;
  TextArea    viewApptTextBox;

  // Add Appointment
  VerticalPanel   addApptPanel;
  TextBox     ownerBox;
  TextBox     descriptionBox;
  TimeFields  beginTimeFields;
  TimeFields  endTimeFields;
  Button      addApptButton;

  // Search Appointments
  VerticalPanel   searchPanel;
  TextBox     searchOwnerBox;
  TimeFields  searchBeginTimeFields;
  TimeFields  searchEndTimeFields;
  Button      searchButton;
  TextArea    searchTextArea;

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
    helpButton.setWidth("90px");
    helpButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        setPanelsFalse();
        helpPanel.setVisible(true);
        printReadme();
      }
    });
    helpPanel = new HorizontalPanel();
    helpMenu = new VerticalPanel();
    helpTextArea = new TextArea();

    viewApptHelp = new Button("View Help");
    viewApptHelp.setWidth("90px");
    viewApptHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setText("View Appointment Book Help:\n" +
                "The View Appointment Book page will let you view a single appointment book or all of the\n" +
                "appointment books stored on the site. The dropdown menu on the left of the page will let\n" +
                "you select a particular owner or all. When you click on your choice a text window will\n" +
                "appear containing the related appointments.\n"
        );
      }
    });

    addApptHelp = new Button("Add Help");
    addApptHelp.setWidth("90px");
    addApptHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setText("Add Appointment Help:\n" +
                "The Add Appointment page will assist you in adding appointments to your appointment book.\n" +
                "You can have multiple appointment books as long as they have different owners. If you add\n" +
                "an appointment with an owner that does not yet exist, a new appointment book will be created\n\n" +
                "Requirements: \n" +
                "   Owner: the name of the appointment book\n" +
                "   Description: a description of the appointment\n" +
                "   Begin Time: when the appointment will start\n" +
                "   End Time: when the appointment will end\n"
        );
      }
    });

    searchHelp = new Button("Search Help");
    searchHelp.setWidth("90px");
    searchHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setText("Search Help:\n" +
                "The search option will search for though an Appointment Book for all appointments between\n" +
                "two dates. If the owner doesnt have an appointment book or the owner doesnt have any\n" +
                "appointments between the give dates an appropiate message will be displayed. \n\n" +
                "Requirements: \n" +
                "   Owner: the name of the appointment book to search\n" +
                "   Begin Time: the time equal or less than the appointments to search for\n" +
                "   End Time: the time equal to or greater than the appointments to search for.\n"
        );
      }
    });

    readmeHelp = new Button("ReadMe");
    readmeHelp.setWidth("90px");
    readmeHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setText("Name: DJ Sabo\n" +
                "Project 5: Rich Internet Application For Appointment Book\n"
        );
      }
    });


    // View Appointment
    viewApptsPanel = new HorizontalPanel();
    viewApptTextBox = new TextArea();

    viewAppointmenBookButton = new Button("View Appointment Book");
    viewAppointmenBookButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        setPanelsFalse();
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
        setPanelsFalse();
        addApptPanel.setVisible(true);

      }
    });

    // Add Appt Items
    this.addApptPanel = new VerticalPanel();
    addApptButton = new Button("Add");
    addApptButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (addAppointment()) {
          addApptPanel.setVisible(false);

          // Print out the confirmation message
          StringBuffer output = new StringBuffer();
          output.append(getOwner());
          output.append("'s new appointment was successfully added.");
          mainTextArea.setCharacterWidth(80);
          mainTextArea.setVisibleLines(1);
          mainTextArea.setText(output.toString());
          mainTextPanel.setVisible(true);

          // Clear the text blocks
          ownerBox.setText("");
          descriptionBox.setText("");
          beginTimeFields.reset();
          endTimeFields.reset();

        }
      }
    });
    this.ownerBox = new TextBox();
    this.descriptionBox = new TextBox();
    this.beginTimeFields = new TimeFields();
    this.endTimeFields = new TimeFields();

    // Title Search Button
    searchAppointmentButton = new Button("Search");
    searchAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        setPanelsFalse();
        searchPanel.setVisible(true);
        searchTextArea.setVisible(false);
        searchOwnerBox.setText("");
        searchBeginTimeFields.reset();
        searchEndTimeFields.reset();
      }
    });

    // Search Initializer
    searchPanel = new VerticalPanel();
    searchOwnerBox = new TextBox();
    searchBeginTimeFields = new TimeFields();
    searchEndTimeFields = new TimeFields();
    searchTextArea = new TextArea();

    // Search for Items Button
    searchButton = new Button("Search");
    searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchInRange();
      }
    });

    this.mainTextArea = new TextArea();
    this.mainTextPanel = new DockPanel();
  }

  private void setPanelsFalse(){
    helpPanel.setVisible(false);
    mainTextPanel.setVisible(false);
    addApptPanel.setVisible(false);
    viewApptsPanel.setVisible(false);
    searchPanel.setVisible(false);
  }

  /**
   * This method prints out the readme to the main text box
   */
  private void printReadme() {
    this.mainTextArea.setCharacterWidth(80);
    this.mainTextArea.setVisibleLines(20);
    this.mainTextArea.setText("Name: DJ Sabo\nProject 5: Rich Internet Application For Appointment Book\n");

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

  /**
   * Reads the input from the AddAppointment panel and creates and appointment from it.
   * Then sends the appointment to the server to be added to the data.
   * Returns true if the appointment was successfully added, otherwise false.
   */
  private boolean addAppointment(){
    String owner = getOwner();
    String description = getDescription();
    owner = owner.trim();
    description = description.trim();
    if(owner == null || owner.equals("")){
      alerter.alert("Owner cannot be empty");
      return false;
    }
    if(description == null || description.equals("")){
      alerter.alert("Description cannot be empty");
      return false;
    }
      String beginTime = beginTimeFields.getDate();
      String endTime = endTimeFields.getDate();

    try {
      Appointment appt = new Appointment(description, beginTime, endTime);
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

  /**
   * Searches for all appointments for a given owner in a date range and
   * outputs the results to the screen.
   */
  private void searchInRange() {
    String owner = this.searchOwnerBox.getText();
    owner = owner.trim();
    if(owner == null || owner.equals("")){
      alerter.alert("Owner cannot be empty");
    } else {
      String beginTime = searchBeginTimeFields.getDate();
      String endTime = searchEndTimeFields.getDate();
      this.async.getAppointmentsInRange(owner,beginTime, endTime, new AsyncCallback<String>() {

        @Override
        public void onSuccess(String s) {
          searchTextArea.setVisible(true);
          searchTextArea.setCharacterWidth(60);
          searchTextArea.setVisibleLines(s.split("[\n|\r]").length);
          searchTextArea.setText(s);
        }

        @Override
        public void onFailure(Throwable ex) {
          alert(ex);
        }
      });
    }
  }

  /**
   * Prints the Appointment Book to the screen. If owner is null, prints all books
   * @param owner the name of the Appointment Book to print
     */
  public void viewAppointmentBook(String owner){
    this.async.getAppointmentBook(owner, new AsyncCallback<String>() {

      @Override
      public void onSuccess(String s) {
        viewApptTextBox.setVisible(true);
        viewApptTextBox.setCharacterWidth(60);
        viewApptTextBox.setVisibleLines(s.split("[\n|\r]").length);
        viewApptTextBox.setText(s);
      }
      @Override
      public void onFailure(Throwable ex) {
        alert(ex);
      }
    });
  }

  /**
   * Returns the string contained in the ownerBox textbox
   */
  private String getOwner(){
    return this.ownerBox.getText();
  }

  /**
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

  /**
   * Sets up the Root Panel and adds panels for Help, View Appointment Books,
   * Add Appointments, and  Search
   */
  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();
    rootPanel.add(helpButton);
    rootPanel.add(viewAppointmenBookButton);
    rootPanel.add(addAppointmentButton);
    rootPanel.add(searchAppointmentButton);

    // Setup the Help Menu
    helpMenu.add(viewApptHelp);
    helpMenu.add(addApptHelp);
    helpMenu.add(searchHelp);
    helpMenu.add(readmeHelp);
    helpPanel.add(helpMenu);
    helpTextArea.setCharacterWidth(100);
    helpTextArea.setVisibleLines(30);
    helpPanel.add(helpTextArea);
    helpPanel.setVisible(false);
    rootPanel.add(helpPanel);


    // Text panel for printing readme and appointment book
    mainTextPanel.setVisible(false);
    mainTextPanel.add(this.mainTextArea, DockPanel.CENTER);
    rootPanel.add(mainTextPanel);

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
    addApptPanel.add(apptPanel1);
    addApptPanel.add(beginTimeFields.createDatePanel("Begin"));
    addApptPanel.add(endTimeFields.createDatePanel("...End"));
    addApptPanel.add(addApptButton);
    rootPanel.add(this.addApptPanel);

    // Setup Search Panel
    searchPanel.setVisible(false);
    HorizontalPanel searchPan1 = new HorizontalPanel();
    searchPan1.add(new Label("Owner"));
    searchPan1.add(searchOwnerBox);
    searchPanel.add(searchPan1);
    searchPanel.add(searchBeginTimeFields.createDatePanel("Begin"));
    searchPanel.add(searchEndTimeFields.createDatePanel("...End"));
    searchPanel.add(searchButton);
    searchTextArea.setVisible(false);
    searchPanel.add(searchTextArea);
    rootPanel.add(searchPanel);

  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}
