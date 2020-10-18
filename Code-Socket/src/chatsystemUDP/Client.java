package chatsystemUDP;

import java.io.*;
import java.net.*;
import chatsystemUDP.ReceivingThread;
import chatsystemUDP.SendingThread;

/**
 * Client est la classe qui représente un client qui se connecte au chat. 
 * Elle s'occupe de créer les threads d'écoute et d'envoi.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class Client {
  
  // ----- Variables globales de Client -----

  /**
   * Thread qui va permettre de recevoir les messages
   */
  static ReceivingThread RT;

  /**
   * Thread qui va permettre d'envoyer des messages
   */
  static SendingThread ST;

  // ----- Méthodes -----

  /**
  * Méthode de déconnexion : interrompt les threads.
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
   * Méthode main : Lance les threads de lecture et d'écriture.
   * @param args Contient en premier l'adresse du groupe, en deuxième le numéro de port du groupe 
   * et en dernier le pseudo du client.
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      System.out.println("Usage: java Client <Group address> <Group port> <Pseudo>");
      System.exit(1);
    }
    try {
      InetAddress groupeIP = InetAddress.getByName(args[0]);
      RT = new ReceivingThread(groupeIP, new Integer(args[1]).intValue());
      ST = new SendingThread(groupeIP, new Integer(args[1]).intValue(), args[2]);
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host : " + args[0]);
      System.exit(1);
    }
  }
}