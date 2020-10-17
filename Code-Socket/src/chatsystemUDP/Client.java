package chatsystemUDP;

import java.io.*;
import java.net.*;
import chatsystemUDP.ReceivingThread;
import chatsystemUDP.SendingThread;

public class Client {
  
  // Variables globales de Client

  static ReceivingThread RT; // Thread qui va permettre de recevoir les messages
  static SendingThread ST; // Thread qui va permettre d'envoyer des messages
  static boolean waitForHistory; //Booléen qui permet de savoir si on a reçu l'historique

  // Méthodes 

  /**
  * disconnect method
  * Interrupt the reading and writing threads and close the socket
  **/
  public static void disconnect() {
    try {
      RT.interrupt();
      ST.interrupt();
    } catch (Exception e) {
      System.out.println("Error when disconnecting : " + e);
    }
  }
 
  /**
  * main method
  * Collect the parameters 
  * and launch the reading and writing threads
  **/
  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      System.out.println("Usage: java Client <Server host> <Server port> <pseudo>");
      System.exit(1);
    }
    try {
      InetAddress groupeIP = InetAddress.getByName(args[0]);
      RT = new ReceivingThread(groupeIP, new Integer(args[1]).intValue());
      ST = new SendingThread(groupeIP, new Integer(args[1]).intValue(), args[2]);
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host:" + args[0]);
      System.exit(1);
    }
  }
}


