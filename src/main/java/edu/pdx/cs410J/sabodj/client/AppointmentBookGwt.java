package edu.pdx.cs410J.sabodj.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  @VisibleForTesting
  Button      button;
  Button      helpButton;
  Button      viewAppointmenBookButton;
  Button      addAppointmentButton;
  Button      searchAppointmentButton;

  DockPanel   textOnlyPanel;

  VerticalPanel   addApptPanel;
  TextBox     ownerBox;
  TextBox     descriptionBox;
  ListBox     beginDay;
  ListBox     beginMonth;
  ListBox beginYear;
  ListBox     beginHour;
  ListBox     beginMinute;
  ListBox beginAMPM;
  ListBox     endDay;
  ListBox     endMonth;
  ListBox     endYear;
  ListBox     endHour;
  ListBox     endMinute;
  ListBox endAMPM;

  Button      addApptButton;

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
    helpButton = new Button("Help");
    helpButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
        searchButton.setVisible(false);
        searchPanel.setVisible(false);
        textOnlyPanel.setVisible(true);
        printReadme();
      }
    });

    viewAppointmenBookButton = new Button("View Appointment Book");
    viewAppointmenBookButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
        searchButton.setVisible(false);
        searchPanel.setVisible(false);
        textOnlyPanel.setVisible(true);
        viewAppointments();
      }
    });

    addAppointmentButton = new Button("Add Appointment");
    addAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        textOnlyPanel.setVisible(false);
        searchButton.setVisible(false);
        searchPanel.setVisible(false);
        addApptPanel.setVisible(true);
        addApptButton.setVisible(true);
      }
    });

    searchAppointmentButton = new Button("Search");
    searchAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        textOnlyPanel.setVisible(false);
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
        searchPanel.setVisible(true);
        searchButton.setVisible(true);
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
          output.append("'s new appointment was added to his appointment book.");
          mainTextArea.setCharacterWidth(50);
          mainTextArea.setVisibleLines(1);
          mainTextArea.setText(output.toString());
          textOnlyPanel.setVisible(true);

          // Clear the text blocks
          ownerBox.setText("");
          descriptionBox.setText("");
        }
      }
    });
    this.ownerBox = new TextBox();
    this.descriptionBox = new TextBox();
    this.beginDay = setDays();
    this.beginMonth = setHoursMonths();
    this.beginYear = setYears();
    this.beginHour = setHoursMonths();
    this.beginMinute = setMinutes();
    this.beginAMPM = setAMPM();
    this.endDay = setDays();
    this.endMonth = setHoursMonths();
    this.endYear = setYears();
    this.endHour = setHoursMonths();
    this.endMinute = setMinutes();
    this.endAMPM = setAMPM();

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

    button = new Button("Ping Server");
    button.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointments();
      }
    });

    this.mainTextArea = new TextArea();

    
    this.textBox = new TextBox();

    this.textOnlyPanel = new DockPanel();
  }

  /**
   * returns a ListBox containing  1-12 for a hour/month selection
   */
  private ListBox setHoursMonths(){
    ListBox list = new ListBox();
    for(int i= 1; i < 13; i++){
      list.addItem(Integer.toString(i));
    }
    return list;
  }

  /**
   * returns a ListBox containing 1-59 for a minute selection
   */
  private ListBox setMinutes(){
    ListBox list = new ListBox();
    for(int i= 0; i < 60; i++){
      list.addItem(Integer.toString(i));
    }
    return list;
  }

  /**
   * Returns a ListBox containing 1-31 for a day selection
   */
  private ListBox setDays(){
    ListBox list = new ListBox();
    for(int i= 1; i < 32; i++){
      list.addItem(Integer.toString(i));
    }
    return list;
  }

  /**
   * Returns a ListBox containing the years 2010-2020
   */
  private ListBox setYears(){
    ListBox list = new ListBox();
    for(int i= 2010; i < 2021; i++){
      list.addItem(Integer.toString(i));
    }
    return list;
  }

  /**
   * Returns a ListBox containing AM and PM
   */
  private ListBox setAMPM(){
    ListBox list = new ListBox();
    list.addItem("AM");
    list.addItem("PM");
    return list;
  }






  private void printReadme() {
    this.mainTextArea.setCharacterWidth(80);
    this.mainTextArea.setVisibleLines(20);
    this.mainTextArea.setText("Name: DJ Sabo\nProject 5: Rich Internet Application For Appointment Book\n");

  }

  private void viewAppointments() {
    this.mainTextArea.setCharacterWidth(120);
    this.mainTextArea.setVisibleLines(40);
    this.mainTextArea.setText("Need to print out all appointments");
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
    return true;
  }


  private void createAppointments() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    int numberOfAppointments = getNumberOfAppointments();
    async.createAppointmentBook(numberOfAppointments, new AsyncCallback<AppointmentBook>() {

      @Override
      public void onSuccess(AppointmentBook airline) {
        displayInAlertDialog(airline);
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
    textOnlyPanel.setVisible(false);
    textOnlyPanel.add(this.mainTextArea, DockPanel.CENTER);
    rootPanel.add(textOnlyPanel);

    // Setup Add Appointment panel
    addApptPanel.setVisible(false);
    HorizontalPanel apptPanel1 = new HorizontalPanel();
    apptPanel1.add(new Label("Owner"));
    apptPanel1.add(this.ownerBox);
    apptPanel1.add(new Label("Description"));
    apptPanel1.add(this.descriptionBox);
    HorizontalPanel apptPanel2 = new HorizontalPanel();
    apptPanel2.add(new Label("Begin Date"));
    apptPanel2.add(this.beginMonth);
    apptPanel2.add(this.beginDay);
    apptPanel2.add(this.beginYear);
    apptPanel2.add(new Label("Begin Time"));
    apptPanel2.add(this.beginHour);
    apptPanel2.add(this.beginMinute);
    apptPanel2.add(this.beginAMPM);
    HorizontalPanel apptPanel3 = new HorizontalPanel();
    apptPanel3.add(new Label("  End Date"));
    apptPanel3.add(this.endMonth);
    apptPanel3.add(this.endDay);
    apptPanel3.add(this.endYear);
    apptPanel3.add(new Label("  End Time"));
    apptPanel3.add(this.endHour);
    apptPanel3.add(this.endMinute);
    apptPanel3.add(this.endAMPM);
    addApptPanel.add(apptPanel1);
    addApptPanel.add(apptPanel2);
    addApptPanel.add(apptPanel3);
    rootPanel.add(this.addApptPanel);


    // Setup Search  panel
//    searchPanel.setVisible(false);
//    HorizontalPanel searchPan1 = new HorizontalPanel();
//    searchPan1.add(new Label("Owner"));
//    searchPan1.add(this.ownerBox);
//    searchPan1.add(searchPan1);
//    searchPan1.add(apptPanel2);
//    rootPanel.add(searchPanel);


    addApptButton.setVisible(false);
    searchButton.setVisible(false);
    rootPanel.add(addApptButton);
    rootPanel.add(searchButton);


  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}
