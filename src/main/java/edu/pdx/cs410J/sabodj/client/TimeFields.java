package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;


/**
 * This class is designed to using dates. I creates ListBox's for Date and Time fields and a reset()
 * method to set each ListBox to its default value.
 * It also creates a HorizontalPanel with the all of the Date and Time ListBoxes
 */
public class TimeFields {
    ListBox day;
    ListBox month;
    ListBox year;
    ListBox hour;
    ListBox minute;
    ListBox ampm;

    /**
     * Constructor. Creates all of the ListBoxes and initializes them with their values.
     */
    TimeFields(){
        day     = setDays();
        month   = setHoursMonths();
        year    = setYears();
        hour    = setHoursMonths();
        minute  = setMinutes();
        ampm    = setAMPM();
        reset();
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
        list.addItem("00");
        list.addItem("05");
        for(int i= 10; i < 60; i+=5){
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

    /**
     * Resets the time values to default
     */
     void reset() {
        day.setSelectedIndex(0);
        month.setSelectedIndex(0);
        year.setSelectedIndex(0);
        hour.setSelectedIndex(11);
        minute.setSelectedIndex(0);
        ampm.setSelectedIndex(0);

    }

    /**
     * Retunrs a formatted date (MM/DD/YYYY hh:mm
     */
    String getDate(){
        StringBuilder date = new StringBuilder();
        date.append(month.getSelectedItemText());
        date.append("/");
        date.append(day.getSelectedItemText());
        date.append("/");
        date.append(year.getSelectedItemText());
        date.append(" ");
        date.append(hour.getSelectedItemText());
        date.append(":");
        date.append(minute.getSelectedItemText());
        date.append(" ");
        date.append(ampm.getSelectedItemText());
        return date.toString();
    }

    /**
     * @param name if its a begin time or end time
     * Returns a horizontal panel with date buttons and labels
     */
    HorizontalPanel createDatePanel(String name){
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(new Label(name + " Date"));
        panel.add(this.month);
        panel.add(new Label("/"));
        panel.add(this.day);
        panel.add(new Label("/"));
        panel.add(this.year);
        panel.add(new Label("..." + name + " Time"));
        panel.add(this.hour);
        panel.add(new Label(":"));
        panel.add(this.minute);
        panel.add(this.ampm);
        return panel;

    }

}
