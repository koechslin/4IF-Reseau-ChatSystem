package chatsystemUDP;

import java.io.*;
import java.net.*;

public class ReceivingThread extends Thread {

	// Attributs du thread client de réception
	InetAddress groupIP;
	int port;
	MulticastSocket socket;
	boolean historyOk; // Booléen qui permet de savoir si on a reçu l'historique

	/**
    * interrupt method
    * Leave group and close the socket
	**/
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
	* constructor
	* @param groupIP the virtual multicast IP address 
	* @param port the port number of the group
	**/
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
	* run method
	* Wait for a message in the multicastSocket
	* and write it on the console
  	**/
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
					//message normal reçu
					System.out.println(msg);
				}

				if(typeMessage.equals("3") && historyOk == false){
					// hitorique reçu
					if(!msg.equals("")){
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



  
