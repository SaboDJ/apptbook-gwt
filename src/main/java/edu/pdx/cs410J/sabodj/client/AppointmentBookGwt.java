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

  DockPanel   addApptPanel;
  TextBox     ownerBox;
  TextBox     descriptionBox;
  TextBox     beginDate;
  TextBox     beginTime;
  TextBox     endDate;
  TextBox     endTime;
  Button      addApptButton;

  DockPanel   searchPanel;
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
        addApptPanel.setVisible(true);
        addApptButton.setVisible(true);
        addAppointment();
      }
    });

    searchAppointmentButton = new Button("Search");
    searchAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addApptPanel.setVisible(false);
        addApptButton.setVisible(false);
       // searchPanel.setVisible(true);
        searchButton.setVisible(true);
       // Call the search method
      }
    });

    // Add Appt Items
    addApptButton = new Button("Add");
    addApptButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addApptButton.setVisible(false);
        addApptPanel.setVisible(false);
        //addAppointmentToBook();
      }
    });
    this.ownerBox = new TextBox();
    this.descriptionBox = new TextBox();
    this.beginDate = new TextBox();
    this.beginTime = new TextBox();
    this.endDate = new TextBox();
    this.endTime = new TextBox();

    // Search Items
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
    this.addApptPanel = new DockPanel();
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

  private void addAppointment() {
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
    rootPanel.add(button);

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
    addApptPanel.add(new Label("Owner"), DockPanel.WEST);
    addApptPanel.add(this.ownerBox, DockPanel.EAST);
//    addApptPanel.add(new Label("Description"), DockPanel.EAST);
//    addApptPanel.add(this.descriptionBox, DockPanel.WEST);
    rootPanel.add(this.addApptPanel);


//    // Setup Search Panel
//    searchPanel.setVisible(false);
//    searchPanel.add(new Label("Owner"), DockPanel.NORTH);
//    addApptPanel.add(this.ownerBox, DockPanel.SOUTH);


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
