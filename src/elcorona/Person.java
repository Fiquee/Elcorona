/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcorona;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.AttributedCharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Forge-15 i7
 */
public class Person implements Comparable<Person> {

    int x, y;       //Coordinate
    int Orivx, Orivy, vx, vy;     //Velocity
    double VelocityX, VelocityY;
    double averageS, averageV;
    int status, mapType;
    double probInfection;
    boolean suspected = false;
    boolean infected = false;
    boolean dead = false;
    boolean socialDistancing;
    long symptomsTime = 0, FirstDayInfected = 0,deadTime = 0;
    double ContactTime;
    String location;
    String ID, occupation;
    static int add = 0;

    JSONArray individualLogs = new JSONArray();
    LinkedList log = new LinkedList();
    LinkedList<String> connections = new LinkedList();
    Random r = new Random();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    String type;  //Old woman, Old man, man, woman, children -- probInfection

    public Person(int status, String ID, int mapType, boolean socialDistancing) {
        this.status = status;
        this.ID = ID;
        this.mapType = mapType;
        this.socialDistancing = socialDistancing;

        type = getType(mapType);

        probInfection = getprobInfection(type);

        occupation = getOccupation(mapType);

        x = r.nextInt(1278) + 2;
        y = r.nextInt(618) + 8;

        location = getLocation(mapType);

        Orivx = (int) (Math.random() * (5) + -2); // between -2 --> 2  
        Orivy = (int) (Math.random() * (5) + -2); // between -2 --> 2  
        VelocityX = (double) Orivx / 16;
        VelocityY = (double) Orivy / 16;
        averageS = (double) Math.sqrt((Orivx * Orivx) + (Orivy * Orivy));
        averageV = (double) Math.sqrt((VelocityX * VelocityX) + (VelocityY * VelocityY));

    }

    public boolean collision(Person person2, Date date) {
        boolean found = false;
        Rectangle p1 = new Rectangle(this.x, this.y, 10, 10);
        Rectangle p2 = new Rectangle(person2.x, person2.y, 10, 10);
        if (p1.intersects(p2)) {
            if (!this.dead && !person2.dead) {
                if ((this.status != 2 || person2.status != 2)) {
                    String str1 = this.ID + " meets " + person2.ID + " at " + this.getLocation(mapType) + " " + df.format(date) + " " + person2.probInfection + "(" + person2.type + ")";
                    String str2 = person2.ID + " meets " + this.ID + " at " + person2.getLocation(mapType) + " " + df.format(date) + " " + this.probInfection + "(" + this.type + ")";
//                System.out.println(str1);
//                System.out.println(str2);
                    this.log.add(str1);
                    person2.log.add(str2);
                    /*
                    printToFile(this.ID + ".txt", log);                       //Update log file
                    person2.printToFile(person2.ID + ".txt", person2.log);    //Update second's log file
*/
                    logEntry(person2, date);
                    person2.logEntry(this, date);

                    if (this.status == 4 && person2.status == 0) {

                        LinkedList conlist = this.connections;
                        for (int i = 0; i < conlist.size(); i++) {
                            if (person2.ID.equals(conlist.get(i))) {
                                System.out.println();
//                            System.out.println(person2.ID + " is in " + this.ID + " connections");
                                person2.probInfection = 0.7;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            person2.probInfection = getprobInfection(person2.type);
                        }
                        System.out.println();
                        double prob = person2.probInfection * (Math.pow(0.9, ((date.getTime() - this.FirstDayInfected) / 86400000)));
                        double rate = (Math.pow(0.9, ((date.getTime() - this.FirstDayInfected) / 86400000)));
                        System.out.println(person2.ID + " is suspected bcs interacts with the patient [" + this.ID + "]");
                        System.out.println(person2.ID + " probability to get infected is " + person2.probInfection + "*" + rate + " = " + prob);
                        person2.probInfection = prob;
                        person2.suspected = true;
                        person2.FirstDayInfected = date.getTime();
                        person2.symptomsTime = 14 * 86428571; // = 14 days

                        this.status = 2;
                        person2.status = 2;
                        this.ContactTime = averageS / averageV;
                        person2.ContactTime = person2.averageS / person2.averageV;
                        if ((this.vx == 0 && this.vy == 0) && (person2.vx == 0 && person2.vy == 0)) {
                            this.ContactTime = Double.POSITIVE_INFINITY;
                            person2.ContactTime = Double.POSITIVE_INFINITY;
                        } else if ((person2.vx == 0 && person2.vy == 0)) {
                            person2.ContactTime = this.ContactTime;
                        } else if ((this.vx == 0 && this.vy == 0)) {
                            this.ContactTime = person2.ContactTime;
                        }
                        add++;
                        return true;

                    } else if (person2.status == 4 && this.status == 0) {

                        LinkedList conlist = person2.connections;
                        for (int i = 0; i < conlist.size(); i++) {
                            System.out.println();
//                        System.out.println(this.ID + " is in " + person2.ID + " connections");
                            if (this.ID.equals(conlist.get(i))) {
                                this.probInfection = 0.7;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            this.probInfection = getprobInfection(this.type);
                        }
                        System.out.println();
                        double rate = (Math.pow(0.9, ((date.getTime() - person2.FirstDayInfected) / 86400000)));
                        double prob = getprobInfection(this.type) * (Math.pow(0.9, ((date.getTime() - person2.FirstDayInfected) / 86400000)));
                        System.out.println(this.ID + " is suspected bcs interacts with the patient [" + person2.ID + "]");
                        System.out.println(this.ID + " probability to get infected is " + this.probInfection + "*" + rate + " = " + prob);
                        this.probInfection = prob;
                        this.status = 1;
                        this.suspected = true;
                        this.FirstDayInfected = date.getTime();
                        this.symptomsTime = 14 * 86428571;
                        add++;

                        this.status = 2;
                        person2.status = 2;
                        this.ContactTime = averageS / averageV;
                        person2.ContactTime = person2.averageS / person2.averageV;
                        if ((this.vx == 0 && this.vy == 0) && (person2.vx == 0 && person2.vy == 0)) {
                            this.ContactTime = Double.POSITIVE_INFINITY;
                            person2.ContactTime = Double.POSITIVE_INFINITY;
                        } else if ((person2.vx == 0 && person2.vy == 0)) {
                            person2.ContactTime = this.ContactTime;
                        } else if ((this.vx == 0 && this.vy == 0)) {
                            this.ContactTime = person2.ContactTime;
                        }
                        return true;

                    } else {
                        this.status = 2;
                        person2.status = 2;
                        this.ContactTime = averageS / averageV;
                        person2.ContactTime = person2.averageS / person2.averageV;
                        if ((this.vx == 0 && this.vy == 0) && (person2.vx == 0 && person2.vy == 0)) {
                            this.ContactTime = Double.POSITIVE_INFINITY;
                            person2.ContactTime = Double.POSITIVE_INFINITY;
                        } else if ((person2.vx == 0 && person2.vy == 0)) {
                            person2.ContactTime = this.ContactTime;
                        } else if ((this.vx == 0 && this.vy == 0)) {
                            this.ContactTime = person2.ContactTime;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void logEntry(Person p2, Date date){
        JSONObject entry = new JSONObject();
        entry.put("Interaction", p2.ID);
        entry.put("Location", this.getLocation(mapType));
        entry.put("Date", df.format(date));
        entry.put("Infection Probability", p2.probInfection);
        entry.put("Type", p2.type);
        individualLogs.add(entry);
    }

    
    /*
    public void printToFile(String filename, LinkedList log) {
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(filename));
            out.print(log.showList());
            out.close();
        } catch (IOException e) {

        }
    }
*/

    public void paint(Graphics g) {
        if (mapType == 0) {       //rural
            switch (status) {
                case -1:    //dead
                    g.setColor(Color.GRAY);
                    break;
                case 0:     //normal
                    g.setColor(Color.WHITE);
                    break;
                case 1:     //suspected
                    g.setColor(Color.ORANGE);
                    break;
                case 2:     //Contact color
                    g.setColor(Color.BLUE);
                    break;
                case 4:     //Infected
                    g.setColor(Color.red);
                    break;
            }
        } else {        // urban
            switch (status) {
                case -1:    //dead
                    g.setColor(Color.GRAY);
                    break;
                case 0:     //Normal
                    g.setColor(Color.WHITE);
                    break;
                case 1:     //suspected
                    g.setColor(Color.YELLOW);
                    break;
                case 2:     //Contact color
                    g.setColor(Color.BLUE);
                    break;
                case 4:     //Infected
                    g.setColor(Color.red);
                    break;
            }
        }

        if (status == -1) { //if dah mati
            x = x;
            y = y;
            vx = 0;
            vy = 0;
        }

        if (status == 2) {
            ContactTime = ContactTime - 1;
            if (ContactTime <= 0) {
                if (suspected) {
                    status = 1;
                } else if (infected) {
                    status = 4;
                } else if (dead) {
                    status = -1;
                } else {
                    status = 0;
                }
            }
        }

        if (status == 0) {
            probInfection = getprobInfection(type);
        }

        if (!dead) {
            if (socialDistancing) {
                x = x;
                y = y;
                vx = 0;
                vy = 0;
            } else {
                vx = Orivx;
                vy = Orivy;
                x = x + vx;
                y = y + vy;
            }
        }

        //try x < 2 || x >=250 (for one box)
        if (x < 2 || x >= 1280) {
            Orivx *= -1;
        }

        //try y < 8 || y >= 150 (for one box)
        if (y < 8 || y >= 626) {
            Orivy *= -1;
        }
        location = getLocation(mapType);

        g.drawString(location, x - 12, y - 12);
        g.drawString(type, x - 2, y - 3);
        g.drawString(String.valueOf(probInfection), x, y + 30);
        g.drawString(ID, x + 2, y + 20);
        g.fillOval(x, y, 10, 10);
    }

    public String getLocation(int mapType) {
        //x is from 2 - 1280
        //y is from 8 - 626

        if (mapType == 1) {         //urban city
            if (x >= 2 && x < 512 && y >= 8 && y < 163) {
                return "Sekolah";
            } else if (x >= 512 && x < 767 && y >= 8 && y < 163) {
                return "Ofis";
            } else if (x >= 767 && x <= 1280 && y >= 8 && y < 163) {
                return "PasarayaMidin";
            } else if (x >= 2 && x < 512 && y >= 163 && y <= 626) {
                return "TasikRekreasi";
            } else if (x >= 512 && x <= 1280 && y >= 163 && y < 318) {
                return "HospitalBesar";
            } else if (x >= 512 && x <= 1280 && y >= 318 && y <= 626) {
                return "TamanPelangi";
            } else {
                return "";
            }
        } else {           //Rural city
            if (x >= 2 && x < 257 && y >= 8 && y < 318) {
                return "KebunPakMat";
            } else if (x >= 250 && x < 750 && y >= 8 && y < 163) {
                return "JalanZaba";
            } else if (x >= 512 && x < 767 && y >= 163 && y < 318) {
                return "KlinikDesa";
            } else if (x >= 512 && x < 1022 && y >= 318 && y < 473) {
                return "JalanDam";
            } else if (x >= 512 && x < 1022 && y >= 473 && y <= 626) {
                return "BalaiRayaJKK";
            } else if (x >= 750 && x < 1005 && y >= 8 && y < 163) {
                return "KampungMawar";
            } else if (x >= 767 && x <= 1280 && y >= 163 && y < 318) {
                return "HutanSimpan";
            } else if (x >= 1005 && x <= 1280 && y >= 8 && y < 163) {
                return "HutanSimpan";
            } else if (x >= 1022 && x <= 1280 && y >= 318 && y < 473) {
                return "KedaiRanjeet";
            } else if (x >= 1022 && x <= 1280 && y >= 473 && y <= 626) {
                return "PasarTani";
            } else if (x >= 257 && x < 512 && y >= 163 && y < 318) {
                return "JalanTar";
            } else if (x >= 257 && x < 512 && y >= 318 && y < 473) {
                return "BalaiPolis";
            } else if (x >= 257 && x < 512 && y >= 473 && y <= 626) {
                return "KampungPisang";
            } else if (x >= 2 && x < 257 && y >= 318 && y <= 626) {
                return "KampungPisang";
            } else {
                return "";
            }
        }
    }

    public String getType(int mapType) {           //Adult , Teenager , Children , Old
        int rand;
        double random = Math.random();
        if (mapType == 1) {
            if (random < 0.5) { //50% adult
                rand = 0;
            } else if (random >= 0.5 && random < 0.8) { //30% teenager/children
                rand = r.nextInt(2) + 1;
            } else {
                rand = 3; //20% old
            }
            switch (rand) {
                case 0:
                    return "Adult";
                case 1:
                    return "Teenager";
                case 2:
                    return "Children";
                case 3:
                    return "Old";
            }
        } else {
            if (Math.random() < 0.7) {
                rand = 3;
            } else {
                rand = r.nextInt(3);
            }
            switch (rand) {
                case 0:
                    return "Adult";
                case 1:
                    return "Teenager";
                case 2:
                    return "Children";
                case 3:
                    return "Old";
            }
        }
        return "";
    }

    public double getprobInfection(String type) {
        switch (type) {
            case "Adult":
                return 0.45;
            case "Old":
                return 0.6;
            case "Children":
                return 0.15;
            case "Teenager":
                return 0.3;
            default:
                return 0;
        }
    }

    public String getOccupation(int mapType) {
        if (mapType == 1) {
            String[] occupations = {"Doctor", "Teacher", "Clerk", "Cashier", "Officeman", "Engineer", "Constructor"};
            if (type.equalsIgnoreCase("Adult")) {
                if (Math.random() < 0.7) {
                    return occupations[r.nextInt(occupations.length)];
                }
            } else if (type.equalsIgnoreCase("Old")) {
                if (Math.random() < 0.2) {
                    return occupations[r.nextInt(occupations.length)];
                }
            }
        } else {
            String[] occupations = {"Fisherman", "Tailor", "Carpenter", "Farmer", "Lumberjack"};
            if (type.equalsIgnoreCase("Adult")) {
                if (Math.random() < 0.3) {
                    return occupations[r.nextInt(occupations.length)];
                }
            } else if (type.equalsIgnoreCase("Old")) {
                if (Math.random() < 0.5) {
                    return occupations[r.nextInt(occupations.length)];
                }
            }
        }
        return "None";
    }

    public boolean getSuspected() {
        return Math.random() < 0.0008; // if math.random generates double < 0.0008 --> true (suspected)
    }

    public void Update() {
        symptomsTime = symptomsTime - 302400;  //-1 day == 5 saat
    }

    public boolean isInfected() {
        return Math.random() < probInfection;
    }

    @Override
    public int compareTo(Person o) {
        if (ID.compareTo(o.ID) == 0) {
            return 0;
        } else {
            if (ID.compareTo(o.ID) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public int getAdd() {
        return add;
    }

    @Override
    public String toString() {
        return ID + "";
    }

}
