package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;
import chatsystemTCP.ClientSendingThread;
import chatsystemTCP.ClientReceivingThread;

public class Client {
  
  // Variables globales de Client
  static Socket socket;
  static ObjectOutputStream socOut;
  static ObjectInputStream socIn;
  static BufferedReader stdIn;
  static ClientSendingThread ST; // Thread qui va permettre de lire ce que l'utilisateur tape
  static ClientReceivingThread RT; // Thread qui va permettre de recevoir les messages du serveur
  static String pseudo;

  // Méthodes 

  /**
  * disconnect method
  * Interrupt the reading and writing threads and close the socket
  **/
  public static void disconnect() {
    try {
      ST.interrupt();
      RT.interrupt();
      socOut.close();
      socIn.close();
      stdIn.close();
      socket.close();
    } catch (Exception e) {
      System.out.println("Error when disconnecting : " + e);
    }
  }
 
  /**
  * main method
  * Connect the client to the server, open the input and output streams
  * and launch the reading and writing threads
  **/
  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      System.out.println("Usage: java Client <Server host> <Server port> <pseudo>");
      System.exit(1);
    }
    try {
        pseudo = args[2];
        socket = new Socket(args[0],new Integer(args[1]).intValue()); // création socket => connexion
        socIn = new ObjectInputStream(socket.getInputStream());
        socOut = new ObjectOutputStream(socket.getOutputStream());
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        ST = new ClientSendingThread(stdIn, socOut, pseudo);
        RT = new ClientReceivingThread(socIn);
        ST.start();
        RT.start();
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host:" + args[0]);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to:"+ args[0]);
      System.exit(1);
    }
  }
}


