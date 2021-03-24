/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcorona;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author Forge-15 i7
 */
public class Log implements Comparable<Log> {

    String firstID;
    String secondID;
    String location;
    String dateString;
    Date date;
    int day, month, year;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    double probInfection;
    int prio;

    public Log(String firstID, String secondID, String location, String dateString, String prob, int prio) throws ParseException {
        this.firstID = firstID;
        this.secondID = secondID;
        this.location = location;
        this.dateString = dateString;
        String probability = prob.substring(0, prob.indexOf('('));
        this.probInfection = Double.parseDouble(probability);
        this.prio = prio;

        date = df.parse(dateString);
        day = date.getDate();
        month = date.getMonth();
        year = date.getYear();
    }

    public String getFirstID() {
        return firstID;
    }

    public void setFirstID(String firstID) {
        this.firstID = firstID;
    }

    public String getSecondID() {
        return secondID;
    }

    public void setSecondID(String secondID) {
        this.secondID = secondID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Log o) {
        if (year == o.year && month == o.month && day == o.day) {
            if (prio > o.prio) {
                return 1;
            }
            else if (prio == o.prio && prio > 0) {
                return 0;
            }
            else if (prio < o.prio) {
                return -1;
            }
            else{
                if (probInfection > o.probInfection) {
                    return 1;
                } else if (probInfection < o.probInfection) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } else {
            if (year == o.year && month == o.month) {     //Same year and month
                if (day > o.day) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (year == o.year) {     //Same year different month
                if (month > o.month) {
                    return -1;
                } else {
                    return 1;
                }
            } else {                       //Different year
                if (year > o.year) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    @Override
    public String toString() {
        return firstID + " meets " + secondID + " (" + probInfection + ")" + " " + dateString;
    }

}
