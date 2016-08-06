package edu.pdx.cs410J.sabodj.client;

import com.google.gwt.user.client.ui.ListBox;
import com.sun.tools.javac.util.List;

/**
 * Created by Gaming on 8/5/2016.
 */
public class TimeFields {
    ListBox day;
    ListBox month;
    ListBox year;
    ListBox hour;
    ListBox minute;
    ListBox ampm;

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

}
