package elcorona;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
//import java.util.Timer;
import javax.swing.border.EtchedBorder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class elCorona {

    Scanner sc = new Scanner(System.in);
    Graph<Person> Connection = new Graph();
    LinkedList<Person> people = new LinkedList();
    LinkedList<String> IDlist = new LinkedList();
    PriorityQueue<Log> q = new PriorityQueue();
    LinkedList Checklist = new LinkedList();
//    PriorityQueue<Log> Displayq = new PriorityQueue();
    LinkedList<String> deadlist = new LinkedList();
    JSONObject mainLog = new JSONObject();

    Random r = new Random();
    Person infected = null;

    Image ruralmap = null;
    Image urbanmap = null;
    JPanel toolP;
    JLabel day, insertL, MapL, populationL, interactionL, showAllL, showInfectedL, RMOL, suspectedCountL, infectedCountL;
    JLabel searchL, deadL;
    JTextField searchTF;
    JButton insertB, UrbanB, RuralB, showAllB, showInfectedB, RmoBON, RmoBOff, searchB;
    Timer time;
    Map map;
    boolean socialDistancing = false;
//    long count1 = 0;
//    long count2 = 0;
    int max = 0;
    int imported = 0;
    int mapType;
    Date date;
    long mili, starting;
    long interaction = 0;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    double rate = 0.9;
    long SYMPTOMS = 14 * 86428571; //equivalent = 14 days
    int suspectedcount = 0;
    int infectedcount = 0;

    public static void main(String[] args) throws ParseException {
        elCorona corona = new elCorona();
    }

    public elCorona() throws ParseException {
        Initialize();
    }

    public void Initialize() throws ParseException {

        JFrame frame = new JFrame();
//        targettime = 3*86428571;  //min the 14th day - max 20th day (r.nextInt(7) + 14) // (r.nextInt(7) + 14)
        String str = "20/03/2020";
        date = df.parse(str);
        starting = date.getTime();
        day = new JLabel("Date : " + df.format(starting));
        day.setLayout(null);
        day.setBounds(1000, 70, 300, 20);
        frame.add(day);

        deadL = new JLabel("Died : " + deadlist.size());
        deadL.setBounds(1000, 8, 300, 20);

        interactionL = new JLabel("Interaction : " + interaction);
        interactionL.setLayout(null);
        interactionL.setBounds(1000, 26, 300, 20);
        frame.add(interactionL);

        populationL = new JLabel("Population : " + max);
        populationL.setLayout(null);
        populationL.setBounds(1000, 48, 300, 20);
        frame.add(populationL);

        suspectedCountL = new JLabel("Suspected : " + suspectedcount);
        suspectedCountL.setLayout(null);
        suspectedCountL.setBounds(500, 65, 200, 25);

        infectedCountL = new JLabel("Infected : " + infectedcount);
        infectedCountL.setLayout(null);
        infectedCountL.setBackground(Color.red);
        infectedCountL.setBounds(620, 65, 200, 25);

        insertL = new JLabel("Insert");
        insertL.setBounds(17, 5, 100, 10);

        MapL = new JLabel("Choose Map :");
        MapL.setBounds(140, 5, 100, 15);

        showAllL = new JLabel("<html>Show all<br>Connections : </html>");
        showAllL.setBounds(260, 5, 150, 50);

        showInfectedL = new JLabel("<html>Show Infected&<br>Suspected : </html>");
        showInfectedL.setBounds(260, 45, 150, 50);

        RMOL = new JLabel("Restricted Movement Order");
        RMOL.setBounds(510, 5, 160, 20);

        searchL = new JLabel("SEARCH");
        searchL.setBounds(820, 8, 100, 20);

        searchTF = new JTextField("Type person's id to search");
        searchTF.setBounds(730, 25, 230, 35);
        searchTF.setEnabled(false);

        searchTF.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                searchTF.setText("");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (searchTF.getText().isBlank()) {
                    searchTF.setText("Type person's id to search");
                }
            }
        });

        searchB = new JButton("search");
        searchB.setBounds(795, 60, 100, 25);
        searchB.setEnabled(false);

        RmoBON = new JButton("On");
        RmoBON.setBounds(480, 30, 100, 30);
        RmoBON.setLayout(null);
        RmoBON.setEnabled(true);

        RmoBOff = new JButton("Off");
        RmoBOff.setBounds(593, 30, 100, 30);
        RmoBOff.setLayout(null);
        RmoBOff.setEnabled(true);

        showAllB = new JButton("SHOW");
        showAllB.setBounds(350, 15, 100, 30);
        showAllB.setLayout(null);
        showAllB.setEnabled(false);

        showInfectedB = new JButton("SHOW");
        showInfectedB.setBounds(350, 55, 100, 30);
        showInfectedB.setLayout(null);
        showInfectedB.setEnabled(false);

        insertB = new JButton("INSERT");
        insertB.setBounds(15, 30, 100, 40);
        insertB.setLayout(null);
        insertB.setEnabled(false);

        UrbanB = new JButton("URBAN");
        UrbanB.setBounds(140, 20, 100, 30);
        UrbanB.setLayout(null);

        RuralB = new JButton("RURAL");
        RuralB.setBounds(140, 55, 100, 30);
        RuralB.setLayout(null);

        toolP = new JPanel();
        toolP.setLayout(null);
        toolP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "."));
        toolP.setBounds(5, 5, 700, 80);
        toolP.add(RmoBON);
        toolP.add(RmoBOff);
        toolP.add(RuralB);
        toolP.add(UrbanB);
        toolP.add(insertB);
        toolP.add(insertL);
        toolP.add(MapL);
        toolP.add(showAllL);
        toolP.add(showInfectedL);
        toolP.add(RMOL);
        toolP.add(showAllB);
        toolP.add(showInfectedB);
        toolP.add(suspectedCountL);
        toolP.add(infectedCountL);
        toolP.add(searchL);
        toolP.add(searchTF);
        toolP.add(searchB);
        toolP.add(deadL);

        map = new Map(null);
        map.setLayout(null);
        map.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Map"));
        map.setBounds(2, 85, 1283, 627);
        frame.add(map);

        RmoBON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socialDistancing = true;
                System.out.println("We are on RMO , everyone need to control your movements! : " + socialDistancing);
            }
        });

        RmoBOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socialDistancing = false;
                System.out.println("RMO is suspended till be inform ! : " + socialDistancing);

            }
        });

        RuralB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(socialDistancing);
//                RmoBON.setEnabled(false);
//                RmoBOff.setEnabled(false);
                UrbanB.setEnabled(false);
                RuralB.setEnabled(false);
                insertB.setEnabled(true);
                max = r.nextInt(21) + 10;
                mapType = 0;
                try {
                    ruralmap = ImageIO.read(new File("Ruralmap.jpeg"));

                } catch (IOException ex) {
                    Logger.getLogger(elCorona.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                map = new Map(ruralmap);
                map.setLayout(null);
                map.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Map"));
                map.setBounds(2, 85, 1283, 627);
                frame.add(map);
                startSimulation();
            }
        });

        UrbanB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(socialDistancing);
//                RmoBON.setEnabled(false);
//                RmoBOff.setEnabled(false);
                RuralB.setEnabled(false);
                UrbanB.setEnabled(false);
                insertB.setEnabled(true);
                max = r.nextInt(101) + 100;
                mapType = 1;
                try {
                    urbanmap = ImageIO.read(new File("Urbanmap.jpeg"));

                } catch (IOException ex) {
                    Logger.getLogger(elCorona.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                map = new Map(urbanmap);
                map.setLayout(null);
                map.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Map"));
                map.setBounds(2, 85, 1283, 627);
                frame.add(map);
                startSimulation();
            }
        });

        frame.add(toolP);
        frame.setResizable(false);
        frame.setSize(1300, 750);
        frame.setVisible(true);
        frame.setTitle("El Corona");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
    }

    public void startSimulation() {
        showInfectedB.setEnabled(true);
        searchTF.setEnabled(true);
        searchB.setEnabled(true);
        mili = date.getTime();
        while (people.size() != max) {
            String ID = Integer.toString(r.nextInt(max)); 
            if (!IDlist.contains(ID)) {
                IDlist.add(ID);
                people.add(new Person(0, ID, mapType, socialDistancing));
                Connection.addVertice(new Person(0, ID, mapType, socialDistancing));
            }
        }
        PopulationUpdate();

        for (int i = 0; i < people.size(); i++) {
            Person current = people.get(i);
            GraphNode fromNode = Connection.getVertice(current);
            Edge edgeNode = (Edge) fromNode.edge;
            int edgeCount = Connection.edgeCount(edgeNode);
            if (edgeCount >= 7) {
                continue;
            }
            int connection = r.nextInt(7 - edgeCount) + edgeCount; //max = 2 edges r.nextInt(1) = 0/1 + 1 == 1 / 2
            int j = edgeCount;
            while (j < connection) {
                Person connect = people.get(r.nextInt(max));
                if (connect.compareTo(current) == 0) {
                    while (connect.compareTo(current) == 0) {
                        connect = people.get(r.nextInt(max));
                    }
                }

                if (!Connection.Edgecontains(edgeNode, connect)) {
                    Connection.addEdge(current, connect);
                    Connection.addEdge(connect, current);
                    GraphNode curr = Connection.getVertice(current);
                    edgeNode = (Edge) curr.edge;
                    j++;
                }
            }
            while (edgeNode != null) {
                current.connections.add(edgeNode.Edgevertice.data.toString());
                edgeNode = edgeNode.nextEdge;
            }
        }
        showAllB.setEnabled(true);

        //To show all people's connection
//        System.out.println("THE CONNECTIONS : ");
//        Connection.showGraph();
        ActionListener repaint = new ActionListener() {
            // -172800 for 8 secs = -1 
            @Override
            public void actionPerformed(ActionEvent e) {
                mili = mili + 302400;//5 secs 1 day   //172800 = 8 secs = 1 day 21600
                Update();
                map.repaint();
                map.StartRun();
            }
        };
        time = new Timer(16, repaint);

        insertB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = "Imported" + imported;
                Person newPerson = new Person(1, name, mapType, socialDistancing);
                people.add(newPerson);
                System.out.println();
                System.out.println(newPerson.ID + " suspected because come from other country ! " + df.format(new Date(mili)));
                System.out.println(newPerson.ID + " probability to get infected is " + newPerson.probInfection);
                newPerson.suspected = true;
                newPerson.FirstDayInfected = mili; //mili == current time in simulation
                newPerson.symptomsTime = SYMPTOMS;
                suspectedcount++;
                imported++;
                PopulationUpdate();
            }
        });

        time.restart();

        showInfectedB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame infectedframe = new JFrame();
                String str = "Who is infected : \n\n";
                for (int i = 0; i < people.size(); i++) {
                    if (people.get(i).infected) {
                        str = str + people.get(i).ID + " --> ";
                    }
                }

                str = str + "\n\nWho is suspected : \n\n";
                for (int i = 0; i < people.size(); i++) {
                    if (people.get(i).suspected) {
                        str = str + people.get(i).ID + " --> ";
                    }
                }
                if (!deadlist.isEmpty()) {
                    str = str + "\n\nDead list : ";
                    for (int i = 0; i < deadlist.size(); i++) {
                        str = str + deadlist.get(i) + " --> ";
                    }
                }

                JTextArea textArea = new JTextArea(5, 5);
                JScrollPane scrPane = new JScrollPane(textArea);

                scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                textArea.setText(str);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);

                infectedframe.add(scrPane);
                infectedframe.getContentPane().add(scrPane);
                infectedframe.setResizable(false);
                infectedframe.setSize(400, 400);
                infectedframe.setVisible(true);
                infectedframe.setTitle("All Connections");
                infectedframe.setLocationRelativeTo(null);
                infectedframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                infectedframe.getContentPane().setLayout(null);
            }
        });

        showAllB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame allframe = new JFrame();
                String all = Connection.showGraph();

                JTextArea textArea = new JTextArea(5, 5);
                JScrollPane scrPane = new JScrollPane(textArea);

                scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                textArea.setText(all);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);

                allframe.add(scrPane);
                allframe.getContentPane().add(scrPane);
                allframe.setResizable(false);
                allframe.setSize(820, 820);
                allframe.setVisible(true);
                allframe.setTitle("All Connections");
                allframe.setLocationRelativeTo(null);
                allframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                allframe.getContentPane().setLayout(null);
            }
        });

        searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame searchframe = new JFrame();
                boolean found = false;
                String str = "";
                Person searchperson = getPerson(searchTF.getText());

                if (searchperson == null) {

                    str = str + "\n\n" + searchTF.getText() + " is not exist";

                } else {
                    String status = "";
                    switch (searchperson.status) {
                        case -1:
                            status = "dead";
                            break;
                        case 0:
                            status = "Normal";
                            break;
                        case 1:
                            status = "Suspected";
                            break;
                        case 2:
                            status = "In Contact";
                            break;
                        case 4:
                            status = "Infected";
                            break;
                    }
                    if (searchperson.dead) {
                        String location = searchperson.location;
                        str = str + "\n\n" + searchperson.ID + " status : " + status + " on " + df.format(new Date(searchperson.deadTime));
                    } else if (searchperson.suspected || searchperson.infected) {
                        str = str + "\n\n" + searchperson.ID + " status : " + status + " on " + df.format(new Date(searchperson.FirstDayInfected));
                    } else {
                        str = str + "\n\n" + searchperson.ID + " status : " + status + "\n";
                    }

                    str = str + "\nActivity Log : \n\n" + searchperson.log.showList();

//                            if (pq != null) {
//                                while (pq != null) {
//                                    str = str + "\n\nFrom " + df.format(new Date(searchperson.FirstDayInfected)) + " to " + df.format(new Date(searchperson.FirstDayInfected + (14 * 86400000)));
//
//                                    str = str + "\n\n";
//                                    double prob = (pq.peek()).probInfection * (Math.pow(0.9, (((pq.peek()).date.getTime() - searchperson.FirstDayInfected) / 86400000)));
//                                    rate = Math.pow(0.9,((pq.peek()).date.getTime() - searchperson.FirstDayInfected) / 86400000);
//                                    str = str + (pq.peek()).secondID + " " + (pq.peek()).location + " " + (pq.peek()).dateString + " Probability : " + (pq.peek()).probInfection + "*" + rate + " = " + prob + "\n";
//                                    pq.dequeue();
//                                }
//                            } else {
//                                str = str + "\n\n" + searchperson.ID + " does not meet from " + df.format(new Date(searchperson.FirstDayInfected)) + " to " + df.format(new Date(searchperson.FirstDayInfected + (14 * 86400000)));
//                            }
                }

                JTextArea textArea = new JTextArea(5, 5);
                JScrollPane scrPane = new JScrollPane(textArea);

                scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                textArea.setText(str);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);

                searchframe.add(scrPane);
                searchframe.getContentPane().add(scrPane);
                searchframe.setResizable(false);
                searchframe.setSize(400, 400);
                searchframe.setVisible(true);
                searchframe.setTitle(searchTF.getText());
                searchframe.setLocationRelativeTo(null);
                searchframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                searchframe.getContentPane().setLayout(null);

            }
        });

        searchTF.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { //if enter is pressed
                    JFrame searchframe = new JFrame();
                    boolean found = false;
                    String str = "";
                    Person searchperson = getPerson(searchTF.getText());

                    if (searchperson == null) {
                        
                        str = str + "\n\n" + searchTF.getText() + " is not exist";

                    } else {
                        String status = "";
                        switch (searchperson.status) {
                            case -1:
                                status = "dead";
                                break;
                            case 0:
                                status = "Normal";
                                break;
                            case 1:
                                status = "Suspected";
                                break;
                            case 2:
                                status = "In Contact";
                                break;
                            case 4:
                                status = "Infected";
                                break;
                        }
                        if (searchperson.dead) {
                            str = str + "\n\n" + searchperson.ID + " status : " + status + " on " + df.format(new Date(searchperson.deadTime));
                        } else if (searchperson.suspected || searchperson.infected) {
                            str = str + "\n\n" + searchperson.ID + " status : " + status + " on " + df.format(new Date(searchperson.FirstDayInfected));
                        } else {
                            str = str + "\n\n" + searchperson.ID + " status : " + status + "\n";
                        }
                        str = str + "\nActivity Log : \n\n" + searchperson.log.showList();

//                            if (pq != null) {
//                                while (pq != null) {
//                                    str = str + "\n\nFrom " + df.format(new Date(searchperson.FirstDayInfected)) + " to " + df.format(new Date(searchperson.FirstDayInfected + (14 * 86400000)));
//
//                                    str = str + "\n\n";
//                                    double prob = (pq.peek()).probInfection * (Math.pow(0.9, (((pq.peek()).date.getTime() - searchperson.FirstDayInfected) / 86400000)));
//                                    rate = Math.pow(0.9,((pq.peek()).date.getTime() - searchperson.FirstDayInfected) / 86400000);
//                                    str = str + (pq.peek()).secondID + " " + (pq.peek()).location + " " + (pq.peek()).dateString + " Probability : " + (pq.peek()).probInfection + "*" + rate + " = " + prob + "\n";
//                                    pq.dequeue();
//                                }
//                            } else {
//                                str = str + "\n\n" + searchperson.ID + " does not meet from " + df.format(new Date(searchperson.FirstDayInfected)) + " to " + df.format(new Date(searchperson.FirstDayInfected + (14 * 86400000)));
//                            }
                    }

                    JTextArea textArea = new JTextArea(5, 5);
                    JScrollPane scrPane = new JScrollPane(textArea);

                    scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    textArea.setText(str);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    searchframe.add(scrPane);
                    searchframe.getContentPane().add(scrPane);
                    searchframe.setResizable(false);
                    searchframe.setSize(400, 400);
                    searchframe.setVisible(true);
                    searchframe.setTitle(searchTF.getText());
                    searchframe.setLocationRelativeTo(null);
                    searchframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    searchframe.getContentPane().setLayout(null);

                }
            }

            @Override

            public void keyReleased(KeyEvent e) {
            }
        }
        );

    }

    class Map extends JPanel {

        Image img;

        public Map(Image img) {
            this.img = img;
            Dimension size = new Dimension(780, 475);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 2, 8, 1281, 627, null);
            for (int i = 0; i < people.size(); i++) {
                if (Math.random() < 0.003) {
                    people.get(i).socialDistancing = socialDistancing;
                }
                people.get(i).paint(g);
            }

            for (int i = 0; i < people.size(); i++) {
                for (int j = i + 1; j < people.size(); j++) {
                    if (people.get(i).collision(people.get(j), date)) {
                        if (Person.add == 1) {
                            suspectedcount++;
                            Person.add = 0;
                        }
                        interaction++;
                        PopulationUpdate();
                    }
                }
            }
        }

        public void StartRun() {
            if (suspectedcount < 1 && infectedcount < 1) {
                Person randomperson = people.get(r.nextInt(people.size()));

                if (randomperson.getSuspected()) {
                    randomperson.suspected = true;
                    randomperson.status = 1;
                    System.out.println();
                    System.out.println(randomperson.ID + " is suspected COVID-19 because he ate a bat " + df.format(mili));
                    System.out.println("Probability for " + randomperson.ID + " to get infected is " + randomperson.probInfection);
                    suspectedcount++;
                    PopulationUpdate();
                    randomperson.symptomsTime = SYMPTOMS;
                    randomperson.FirstDayInfected = date.getTime();
                }
            } else {
                if (Math.random() < 0.3) {
                    Person randomperson = people.get(r.nextInt(people.size()));

                    if (!randomperson.suspected && !randomperson.infected && !randomperson.dead) {
                        if (randomperson.getSuspected()) {
                            randomperson.suspected = true;
                            randomperson.status = 1;
                            System.out.println();
                            System.out.println(randomperson.ID + " is suspected COVID-19 because he ate a bat " + df.format(mili));
                            System.out.println("Probability for " + randomperson.ID + " to get infected is " + randomperson.probInfection);
                            suspectedcount++;
                            PopulationUpdate();
                            randomperson.symptomsTime = SYMPTOMS;
                            randomperson.FirstDayInfected = date.getTime();
                        }
                    }
                }
            }

            for (int i = 0; i < people.size(); i++) {

                if (people.get(i).infected) {
                    Person person = people.get(i);
                    int rateday = (int) ((mili - person.FirstDayInfected) / 86400000);
                    if (rateday >= 30) {
                        if (Math.random() < 0.5) {
                            System.out.println(person.ID + " is dead bcs of CORONA " + df.format(new Date(mili)));
                            person.infected = false;
                            person.dead = true;
                            deadlist.add(person.ID);
                            person.status = -1;
                            person.deadTime = mili;
                            infectedcount--;
                            PopulationUpdate();
                        } else {
                            System.out.println(person.ID + " is recover from CORONA");
                            person.infected = false;
                            person.symptomsTime = 0;
                            person.FirstDayInfected = 0;
                            person.status = 0;
                            infectedcount--;
                            PopulationUpdate();
                        }
                    }
                }

                if (people.get(i).suspected == true) {
                    Person suspectedPerson = people.get(i);
                    suspectedPerson.Update();
                    if (suspectedPerson.symptomsTime <= 0) {

                        if (suspectedPerson.isInfected()) {
                            System.out.println();
                            System.out.println(suspectedPerson.ID + " is CONFIRM POSITIVE COVID-19 " + df.format(new Date(mili)));
                            suspectedPerson.infected = true;
                            suspectedPerson.suspected = false;
                            suspectedPerson.status = 4;
                            infectedcount++;
                            suspectedcount--;
                            PopulationUpdate();
                            getActivityLog(suspectedPerson, suspectedPerson.FirstDayInfected);
                            
                            try {
                                getQueue(suspectedPerson, suspectedPerson.FirstDayInfected);
                            } catch (ParseException ex) {
                                Logger.getLogger(elCorona.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            System.out.println();

                            if (q.isEmpty()) {
//                                if (!Displayq.isEmpty()) {
//                                    System.out.println();
//                                    while (!Displayq.isEmpty()) {
//                                        Person firstperson = getPerson(Displayq.peek().firstID);
//                                        double prob = Displayq.peek().probInfection * (Math.pow(0.9, ((Displayq.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000)));
//                                        rate = Math.pow(0.9, ((Displayq.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000));
//                                        System.out.println(Displayq.peek().secondID + " " + Displayq.peek().dateString + " " + Displayq.peek().probInfection + "*" + rate + " = " + prob);
//                                        Displayq.dequeue();
//                                    }
                                System.out.println(suspectedPerson.ID + " does not meet anyone in 14 days");
                            } else {
                                System.out.println();
                                System.out.println("FROM : " + df.format(new Date(suspectedPerson.FirstDayInfected)) + " - " + df.format(new Date(suspectedPerson.FirstDayInfected + (14 * 86400000))) + " (14 days)");
                                System.out.println();

//                                String currdate = df.format(new Date(suspectedPerson.FirstDayInfected));
//                                long currtime = suspectedPerson.FirstDayInfected;
                                while (!q.isEmpty()) {

//                                    String peekdate = q.peek().dateString;
//
//                                    if (currdate.equals(peekdate)) {
                                    Person firstperson = getPerson(q.peek().firstID);
                                    Person secondperson = getPerson(q.peek().secondID);
                                    if (secondperson.suspected) {
                                        double prob = q.peek().probInfection * (Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000)));
                                        rate = Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000));
                                        System.out.println("Infection probability " + q.peek().secondID + " (" + q.peek().dateString + ") : " + q.peek().probInfection + "*" + rate + " = " + prob + " (but already suspected)");
                                    } else if (secondperson.infected) {
                                        double prob = q.peek().probInfection * (Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000)));
                                        rate = Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000));
                                        System.out.println("Infection probability " + q.peek().secondID + " (" + q.peek().dateString + ") : " + q.peek().probInfection + "*" + rate + " = " + prob + " (but already infected)");
                                    } else if (secondperson.dead) {
                                        double prob = q.peek().probInfection * (Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000)));
                                        rate = Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000));
                                        System.out.println("Infection probability " + q.peek().secondID + " (" + q.peek().dateString + ") : " + q.peek().probInfection + "*" + rate + " = " + prob + " (but already died between the 14 days)");
                                    } else {
                                        double prob = q.peek().probInfection * (Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000)));
                                        rate = Math.pow(0.9, ((q.peek().date.getTime() - firstperson.FirstDayInfected) / 86400000));
                                        System.out.println("Infection probability " + q.peek().secondID + " (" + q.peek().dateString + ") : " + q.peek().probInfection + "*" + rate + " = " + prob);
                                        secondperson.suspected = true;
                                        secondperson.probInfection = prob;
                                        suspectedcount++;
                                        secondperson.status = 1;
                                        PopulationUpdate();
                                        try {
                                            Date fd = df.parse(q.peek().dateString);
                                            secondperson.FirstDayInfected = fd.getTime();
                                            secondperson.symptomsTime = SYMPTOMS - (mili - secondperson.FirstDayInfected);
                                        } catch (ParseException ex) {
                                            Logger.getLogger(elCorona.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        System.out.println(secondperson.ID + " is suspected because interacted with the patient ! (" + suspectedPerson.ID + ")");
//                                    }
//                                    else {
//                                        currtime = currtime + 86400000;
//                                        currdate = df.format(new Date(currtime));
//                                        rate = rate * 0.9;
//                                    }
                                    }
                                    q.dequeue();
                                }
                            }
                        } else {
                            System.out.println();
                            System.out.println(suspectedPerson.ID + " is negative covid-19");
                            suspectedPerson.suspected = false;
                            suspectedPerson.symptomsTime = 0;
                            suspectedPerson.FirstDayInfected = 0;
                            suspectedPerson.status = 0;
                            suspectedcount--;
                            PopulationUpdate();
                        }
                    }
                }
            }
        }
    }

    public void getActivityLog(Person targeted, long firstday) {  // 6/4 --- 20/4 lastday 21/4
        boolean found = false;
        System.out.println();
        LinkedList log = targeted.log;
        long last = (15 * 86400000) + firstday;
        String LD = df.format(last);
        String FD = df.format(new Date(firstday));
        System.out.println("FIRST DAY INFECTED : " + FD);
        System.out.println(targeted.ID + "'s Activity Log for 14 days");
        if (!log.isEmpty()) {
            int end = 0;
            while (end == 0 && firstday < last) {
                
                for (int i = log.size() - 1; i >= 0; i--) {
                    String[] arr = ((String) log.get(i)).split(" ");
                    if (arr[5].compareTo(FD) == 0) {
                        while (arr[5].compareTo(FD) == 0) {
                            i--;
                            if (log.get(i) == null) {
                                break;
                            }
                            arr = (((String) log.get(i)).split(" "));
                        }
                        end = i + 1;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
                firstday = firstday + 86400000;
                FD = df.format(new Date(firstday));

            }
            if (found) {
                for (int j = log.size() - 1; j >= end; j--) {
                    System.out.println();
                    System.out.println(log.get(j));
                }
            } else {
                System.out.println();
                System.out.println("No activity Log in 14 days ");
            }
        }

    }

    public void getQueue(Person targeted, long firstday) throws ParseException {
        boolean found = false;
        LinkedList activitylog = targeted.log;
        LinkedList connectionList = targeted.connections;
        int start = 0;
        long last = (15 * 86400000) + firstday;
        String FD = df.format(new Date(firstday));
        String LD = df.format(new Date(last));

        //To print out the connections of the infected person
//        System.out.println("connections : ");
//        for(int m = 0 ; m < connectionList.size() ; m++){
//            System.out.print(connectionList.get(m) + " ");
//            if((m+1)%10 == 0){
//                System.out.println();
//            }
//        }
        if (!activitylog.isEmpty()) {
            while (start == 0 && firstday < last) {
                for (int j = 0; j < activitylog.size(); j++) {
                    String[] arr = ((String) activitylog.get(j)).split(" ");
                    if (arr[5].equals(FD)) {
                        start = j;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
                firstday = firstday + 86400000;
                FD = df.format(new Date(firstday));
            }
            if (found) {
                for (int i = start; i < activitylog.size(); i++) {

                    int prio = 0;
                    String[] arr = ((String) activitylog.get(i)).split(" ");
                    if (arr[5].compareTo(LD) == 0) {
                        break;
                    }

                    if (Connection.isEdge(targeted, getPerson(arr[2]))) {
                        String type = arr[6].substring(arr[6].indexOf('('), arr[6].length());
                        arr[6] = "0.7" + type;
                        prio = 2;
                    }
//                    for (int j = 0; j < connectionList.size(); j++) {
//                        if (arr[2].equals(connectionList.get(j))) {
////                            System.out.println(arr[2] + " equals with " + connectionList.get(j));
//                            String type = arr[6].substring(arr[6].indexOf('('), arr[6].length());
//                            arr[6] = "0.7" + type;
//                            prio = 2;
//                            break;
//                        }
//                    }

                    Log log = new Log(arr[0], arr[2], arr[4], arr[5], arr[6], prio);
                    if (!Checklist.contains(arr[2])) {
                        Checklist.add(arr[2]);
                        q.enqueue(log);
                    }

                }
                Checklist.clear();
            }
        }
    }

    public int getPersonIndex(Person person) {
        if (person != null) {
            for (int i = 0; i < people.size(); i++) {
                if (person.compareTo(people.get(i)) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Person getPerson(String name) {
        Person currentPerson = null;
        for (int q = 0; q < people.size(); q++) {
            currentPerson = people.get(q);
            if (currentPerson.ID.equals(name)) {
                return currentPerson;
            }
        }
        return null;
    }

    public void PopulationUpdate() {
        interactionL.setText("Interaction : " + interaction);
        populationL.setText("Population : " + max + " + (" + imported + ")" + " - (" + deadlist.size() + ")" + " = " + (people.size() - deadlist.size()));
        suspectedCountL.setText("Suspected : " + suspectedcount);
        infectedCountL.setText("Infected : " + infectedcount);
        deadL.setText("Died : " + deadlist.size());
    }

    public void Update() {
        date = new Date(mili);
        String currdate = df.format(date);
        day.setText("Date : " + currdate);
        
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter f = new FileWriter("SimulationLog.json");
            for(int i = 0; i < people.size(); i++){
                JSONArray temp = people.get(i).individualLogs;
                mainLog.put(people.get(i).ID, temp);
            }
            String data = gson.toJson(mainLog);
            f.write(data);
            f.flush();
            f.close();
        }catch(IOException x){
            x.printStackTrace();
        }
    }

}
