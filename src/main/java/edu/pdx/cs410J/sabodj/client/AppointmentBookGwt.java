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
import java.util.Date;

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
        resetPanels();
        helpPanel.setVisible(true);
        helpTextArea.setVisible(false);
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
        helpTextArea.setVisible(true);
        async.getViewHelp(new AsyncCallback<String>() {

          @Override
          public void onSuccess(String s) {
            helpTextArea.setText(s);
          }
          @Override
          public void onFailure(Throwable ex) {
            alert(ex);
          }
        });
    }});

    addApptHelp = new Button("Add Help");
    addApptHelp.setWidth("90px");
    addApptHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setVisible(true);
        async.getAddHelp(new AsyncCallback<String>() {

          @Override
          public void onSuccess(String s) {
            helpTextArea.setText(s);
          }
          @Override
          public void onFailure(Throwable ex) {
            alert(ex);
          }
        });
      }});

    searchHelp = new Button("Search Help");
    searchHelp.setWidth("90px");
    searchHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setVisible(true);
        async.getSearchHelp(new AsyncCallback<String>() {

          @Override
          public void onSuccess(String s) {
            helpTextArea.setText(s);
          }
          @Override
          public void onFailure(Throwable ex) {
            alert(ex);
          }
        });
      }});

    readmeHelp = new Button("ReadMe");
    readmeHelp.setWidth("90px");
    readmeHelp.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        helpTextArea.setVisible(true);
        async.getReadMe(new AsyncCallback<String>() {

          @Override
          public void onSuccess(String s) {
            helpTextArea.setText(s);
          }
          @Override
          public void onFailure(Throwable ex) {
            alert(ex);
          }
        });
      }});

    // View Appointment
    viewApptsPanel = new HorizontalPanel();
    viewApptTextBox = new TextArea();

    viewAppointmenBookButton = new Button("View Appointment Book");
    viewAppointmenBookButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        resetPanels();
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
        resetPanels();
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
        resetPanels();
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

  private void resetPanels(){
    helpPanel.setVisible(false);
    mainTextPanel.setVisible(false);
    addApptPanel.setVisible(false);
    viewApptsPanel.setVisible(false);
    searchPanel.setVisible(false);
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
      String beginTimeString = searchBeginTimeFields.getDate();
      String endTimeString = searchEndTimeFields.getDate();


      try {
        Date beginTime = Appointment.convertStringToDate(beginTimeString);
        Date endTime = Appointment.convertStringToDate(endTimeString);

        if(beginTime.compareTo(endTime) > 0) {
          alerter.alert("Start time must be before ending time");
          return;
        }

        this.async.getAppointmentsInRange(owner,beginTime, endTime, new AsyncCallback<String>() {

          @Override
          public void onSuccess(String s) {
            searchTextArea.setVisible(true);
            searchTextArea.setCharacterWidth(60);
            searchTextArea.setVisibleLines(s.split("[\n|\r]").length + 1);
            searchTextArea.setText(s);
          }

          @Override
          public void onFailure(Throwable ex) {
            alert(ex);
          }
        });

      } catch (ParseException pe){
        alerter.alert(pe.getMessage());
      }

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
        viewApptTextBox.setVisibleLines(s.split("[\n|\r]").length + 1);
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
    helpTextArea.setVisibleLines(13);
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
