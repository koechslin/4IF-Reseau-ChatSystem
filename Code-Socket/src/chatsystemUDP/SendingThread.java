package chatsystemUDP;

import java.io.*;
import java.net.*;

/**
 * SendingThread représente le thread d'envoi du client. Il s'occupe 
 * d'envoyer les messages au groupe.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class SendingThread extends Thread {

    // ----- Attributs du thread client de lecture -----

    /**
     * Adresse du groupe.
     */
    InetAddress groupIP;

    /**
     * Numéro de port du groupe.
     */
    int port;

    /**
     * Socket permettant d'envoyer les messages au groupe.
     */
    MulticastSocket socket;

    /**
     * Variable permettant la lecture du clavier de l'utilisateur.
     */
    BufferedReader stdIn;

    /**
     * Pseudo du client.
     */
    String pseudo;

    // ----- Méthodes -----
    
    /**
     * Méthode interrupt du thread : interrompt le thread et 
     * ferme la socket.
     */
    @Override
	public void interrupt() {
        super.interrupt();
		this.socket.close();
	}
    
    /**
     * Constructeur : Crée la socket d'envoi et lance le thread.
     * @param groupIP Adresse du groupe.
     * @param port Numéro de port du groupe.
     * @param pseudo Pseudo du client.
     */
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
     * Méthode run du thread : lit la console du client et envoie le 
     * message au groupe.
     */
	public void run() {
        // Code spécial de message afin de demander l'historique au serveur
        String getHistoryMsg = "2";
        DatagramPacket msg = new DatagramPacket(getHistoryMsg.getBytes(), getHistoryMsg.length(), this.groupIP, this.port);
        try {
            this.socket.send(msg);
        }
        catch (Exception e) {
            System.out.println("Interrompu.");
            return;
        }

        while(true) {
            try {
                String line = stdIn.readLine();
                if (line.equals(".")) break;
                line = this.pseudo + " dit : " + line;
                line = line + "1"; // message normal envoyé
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