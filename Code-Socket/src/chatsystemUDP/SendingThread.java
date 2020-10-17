package chatsystemUDP;

import java.io.*;
import java.net.*;

public class SendingThread extends Thread {

    // Attributs du thread client de lecture
    InetAddress groupIP;
    int port;
    MulticastSocket socket;
    BufferedReader stdIn;
    String pseudo;

    // Méthodes
    
    /**
    * interrupt method
    * Close the socket
	**/
    @Override
	public void interrupt() {
        super.interrupt();
		this.socket.close();
	}

	/**
	* constructor
	* @param groupIP the virtual multicast IP address 
	* @param port the port number of the group
	* @param pseudo the client's pseudo
	**/
	SendingThread(InetAddress groupIP, int port, String pseudo) {
        this.groupIP = groupIP;
        this.port = port;
        this.pseudo = pseudo;
        try {
            this.socket = new MulticastSocket(port);
            this.stdIn = new BufferedReader(new InputStreamReader(System.in));
            super.start();
        } catch (Exception e) {
            System.out.println("Error in SendingThread constructor : " + e);
        }
	}

	/**
	* run method
	* Read the console of the client, wait for a message
	* and send it to the group
  	**/
	public void run() {

        //Code spécial de message afin de demander au serveur l'historique
        String getHistoryMsg="2";
        DatagramPacket msg = new DatagramPacket(getHistoryMsg.getBytes(), getHistoryMsg.length(), this.groupIP, this.port);
        try{
            this.socket.send(msg);
        }
        catch (Exception e) {
            System.out.println("Interrompu.");
            return; 
        }

        //start reading the console
        while(true) {
            try {
                String line = stdIn.readLine();
                if (line.equals(".")) break;
                line = this.pseudo + " dit : " + line;
                line=line+"1"; //message normal envoyé
                msg = new DatagramPacket(line.getBytes(), line.length(), this.groupIP, this.port);
                this.socket.send(msg);
            } catch (Exception e) {
                if(!isInterrupted()) {
                    System.out.println("Error in SendingThread : " + e);
                } else {
                    System.out.println("Interrompu.");
                    return;
                }
            }
        }
        Client.disconnect();
	}
}

  
