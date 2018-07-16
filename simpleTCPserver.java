/*
 
  Simple TCP socket testing program: Server
  Author: Chun Fung, Wong

  Last Updated: 15-Jul-2018
  Version 1.0

*/

//import sun.net.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static java.lang.System.out;

public class simpleTCPserver extends JFrame
{
  private java.util.Timer timer;
  private static int port = 5000;
  private static boolean sending = false;
 
  private JTextField p_port;
  private JTextArea messageLog;
  private JButton start, stop;
  private JLabel l_port, l_group;
  private JPanel panel, button;

  private static ServerSocket welcomeSocket;
  private static Socket connectionSocket;
  private static BufferedReader inFromClient;

  public simpleTCPserver()
  {  
    super ("Simple TCP Server 1.0");
    setIconImage( new ImageIcon("mcast.gif").getImage());

    class ButtonListener implements ActionListener
     {
        private String t;

        public ButtonListener(String text)
        {
          this.t = text;
        }

        public void actionPerformed ( ActionEvent e )
        {
          if("start".equals(e.getActionCommand()))
          {
            start.setEnabled(false);
            stop.setEnabled(true);
            port = Integer.parseInt(p_port.getText());

            if (port < 1025 || port > 65535) 
            { if (port < 1025) { port = 1025; p_port.setText("1025");}
              else { port = 65535; p_port.setText("65535");} }

            try
            {
               welcomeSocket = new ServerSocket(port);
               connectionSocket = welcomeSocket.accept();
               sending = true;
            } catch ( Exception ex ) {}
          }
          else
          {
            try
            {
              start.setEnabled(true);
              stop.setEnabled(false);
              sending = false;
              connectionSocket.close();
              welcomeSocket.close();
            } catch ( Exception ex ) {}
          }
        }
     }

    addWindowListener ( new WindowAdapter() 
    {
       public void windowClosing ( WindowEvent e )
       {
           simpleTCPserver.this.dispose();
           System.exit(0);
       }
     } );

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

     panel = new JPanel ( new GridLayout(1,2) );

     l_port = new JLabel("TCP port");
     p_port = new JTextField("5000");

     panel.add(l_port);
     panel.add(p_port);

     button = new JPanel();
     button.setLayout( new FlowLayout(FlowLayout.CENTER) );

     start = new JButton("Start");
     start.addActionListener( new ButtonListener("Start") );
     start.setActionCommand("start");
     button.add( start );

     stop = new JButton("Stop");
     stop.addActionListener( new ButtonListener("Stop") );
     stop.setActionCommand("stop");
     button.add( stop );
     
     Container cp = getContentPane();
     cp.add (logpane, BorderLayout.CENTER);
     cp.add (panel, BorderLayout.NORTH);
     cp.add (button, BorderLayout.SOUTH);

    stop.setEnabled(false); 
    pack();
    setVisible( true );

    timer = new java.util.Timer();

    // Schedule timer thread for sending out fixed number of packets in 1 second
    timer.scheduleAtFixedRate(new Schedule(), 0, 1);

  } // End send()

  class Schedule extends TimerTask
  {

    public void run()
    {

     if(sending)
     {
      try
      {
         String clientSentence;

         while (sending)
         {
           inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
           clientSentence = inFromClient.readLine();
         // System.out.println("Received: " + clientSentence);

           messageLog.setText(clientSentence);
         }

       } catch (Exception e){}
      }
    } // End run()
  }

  public static void main(String[] args)
  {
    new simpleTCPserver();
  }
}
