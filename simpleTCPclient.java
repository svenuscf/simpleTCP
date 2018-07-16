/*

  Simple TCP socket testing program: Client
  Author: Chun Fung, Wong

  Last Updated: 15-Jul-2018
  Version 1.0

*/

import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static java.lang.System.out;

public class simpleTCPclient extends JFrame
{
  private static java.util.Timer timer, rpkt_thread;
  private static String group = "127.0.0.1";
  private static String log = "";
  private static int port = 5000;
  private static boolean receiving = false;
  private static Color RED, GREEN;

  private static JTextField t_group, t_port;
  private static JTextArea messageLog;
  private static JButton start, stop, reset, clear; 
  private static JLabel l_group, l_port;

  private static Socket clientSocket;


  public simpleTCPclient()
  {
    super ("Simple TCP Client 1.0");
    setIconImage ( new ImageIcon("mcast.gif").getImage());

    class ButtonListener implements ActionListener
    {
       public ButtonListener(){}

       public void actionPerformed ( ActionEvent e)
       {
          if ("Connect".equals(e.getActionCommand()))
          {
             group = t_group.getText(); 
             port = Integer.parseInt(t_port.getText());
             if (port < 1024 || port > 65535) 
             { port = 5000; t_port.setText("5000"); }
             start.setEnabled(false);
             stop.setEnabled(true);
             receiving = true;
           
             try
             { 
               BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
               clientSocket = new Socket(group, port);
             } catch ( Exception ex ) {}

          }
          else if ("Close".equals(e.getActionCommand()))
          {
             try
             {
                clientSocket.close();
                receiving = false;
                start.setEnabled(true);
                stop.setEnabled(false);

             } catch ( Exception ex ) {}

          }
          else if ("Send".equals(e.getActionCommand()))
          {
             try
             { 
               DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
               outToServer.writeBytes(messageLog.getText() + 'n');
             } catch ( Exception ex ) {}

          }
          else 		//Send timestamp
          {
 
             Calendar now = Calendar.getInstance();
             log = "New timestamp at: " + now.get(Calendar.DAY_OF_MONTH) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + "\n";
             messageLog.setText(log);
          }
       }
    } // End ButtonListener
  
    addWindowListener ( new WindowAdapter()
    {
       public void windowClosing ( WindowEvent e)
       {
          simpleTCPclient.this.dispose();
          System.exit(0);
       }
    });

    messageLog = new JTextArea();
    messageLog.setLineWrap(true);
    messageLog.setWrapStyleWord(true);
    messageLog.setEditable(false);

    JScrollPane logpane = new JScrollPane(messageLog);
    logpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    logpane.setPreferredSize(new Dimension(400, 200));
    logpane.setBorder(BorderFactory.createCompoundBorder(
                       BorderFactory.createCompoundBorder(
                          BorderFactory.createTitledBorder("Message Log"),
                          BorderFactory.createEmptyBorder(3,3,3,3)),
                       logpane.getBorder()));
    
    JPanel panel = new JPanel ( new GridLayout (2, 2) );

    l_group = new JLabel("TCP Server Host");
    l_port = new JLabel("Port");

    t_group = new JTextField("127.0.0.1");
    t_port = new JTextField("5000");
  
    panel.add(l_group);
    panel.add(t_group);
    panel.add(l_port);
    panel.add(t_port);
    
    JPanel button = new JPanel ();
    button.setLayout( new FlowLayout(FlowLayout.CENTER));

    start = new JButton("Connect");
    start.addActionListener( new ButtonListener() );
    start.setActionCommand("Connect");
    button.add( start );

    stop = new JButton("Close");
    stop.addActionListener( new ButtonListener() );
    stop.setActionCommand("Close");
    button.add( stop );
    stop.setEnabled(false);

    reset = new JButton("Send");
    reset.addActionListener( new ButtonListener() );
    reset.setActionCommand("Send");
    button.add( reset );

    clear = new JButton("Timestamp");
    clear.addActionListener( new ButtonListener() );
    clear.setActionCommand("clear");
    button.add( clear );

    Container cp = getContentPane();
    cp.add (logpane, BorderLayout.CENTER);
    cp.add (panel, BorderLayout.NORTH);
    cp.add (button, BorderLayout.SOUTH);

    pack();
    setVisible(true);

    RED = new Color(255, 158, 158);
    GREEN = new Color(125, 249, 150);
       
  }

 
   public static void main(String[] args)
   {
       new simpleTCPclient();

   } //End main()
}
