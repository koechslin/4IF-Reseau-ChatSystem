package chatsystemUDP;

import java.io.*;
import java.net.*;

/**
 * ReceivingThread représente le thread d'écoute du client. Il s'occupe 
 * de recevoir les messages et de les afficher.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class ReceivingThread extends Thread {

	// ----- Attributs du thread client de réception -----
	
	/**
	 * Adresse du groupe.
	 */
	InetAddress groupIP;

	/**
	 * Numéro de port du groupe.
	 */
	int port;

	/**
	 * Socket permettant de recevoir les messages du groupe.
	 */
	MulticastSocket socket;

	/**
	 * Booléen qui permet de savoir si le client a reçu l'historique.
	 */
	boolean historyOk;

	// ----- Méthodes -----

	/**
	 * Méthode interrupt du thread : quitte le groupe et ferme la socket.
	 */
	@Override
	public void interrupt() {
		super.interrupt();
		try {
			this.socket.leaveGroup(this.groupIP);
		} catch (Exception e) {
			System.out.println("Error when interrupting ReceivingThread : " + e);
		}
		this.socket.close();
	}

	/**
	 * Constructeur : crée la socket et rejoint le groupe.
	 * @param groupIP L'adresse IP virtuelle de multicast (adresse du groupe).
	 * @param port Le numéro de port du groupe.
	 */
	ReceivingThread(InetAddress groupIP, int port) {
		this.groupIP = groupIP;
		this.port = port;
		historyOk = false;
		try {
			this.socket = new MulticastSocket(port);
			this.socket.joinGroup(groupIP);
			super.start();
		} catch (Exception e) {
			System.out.println("Error in ReceivingThread constructor : " + e);
		}
	}
	  
	/**
	 * Méthode run du thread : attend la réception d'un message depuis le groupe 
	 * puis l'affiche sur la console.
	 */
	public void run() {
		while(true) {
			byte[] buf = new byte[1024];
			DatagramPacket msgRecv = new DatagramPacket(buf, buf.length);
			try {
				this.socket.receive(msgRecv);
				String msg = new String(msgRecv.getData(), "UTF-8");
				msg = msg.replaceAll("\u0000.*", ""); // enlève les caractères null

				String typeMessage = msg.substring(msg.length()-1); //identifie le type de message
				msg = msg.substring(0, msg.length()-1);

				if(typeMessage.equals("1") && historyOk == true) {
					// message normal reçu
					System.out.println(msg);
				}

				if(typeMessage.equals("3") && historyOk == false) {
					// hitorique reçu
					if(!msg.equals("")) {
						System.out.println(msg);
					}
					historyOk = true;
				}

			} catch (Exception e) {
				if(!isInterrupted()) {
					System.out.println("Error in ReceivingThread : " + e);
				} else {
					System.out.println("Interrompu.");
					return;
				}
			}
		}	
	}
}