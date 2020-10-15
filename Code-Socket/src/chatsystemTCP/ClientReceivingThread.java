package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;

public class ClientReceivingThread extends Thread {

	// Attribut du thread client de réception
	private ObjectInputStream  socIn;
	
	/**
	* constructor
	* @param in the input object stream
  	**/
	ClientReceivingThread(ObjectInputStream in) {
		this.socIn = in;
	}

	/**
	* run method
	* Read the object stream, wait for a Message sent by the server
	* and write it on the console
  	**/
	public void run() {
		try{
			while(true){
				Message msgReceived = (Message) socIn.readObject();
				if (msgReceived.getType() == 1) {
					// Message de type information
					System.out.println(msgReceived.getContent());
				} else {
					// Message normal
					System.out.println(msgReceived.getPseudo() + " dit : " + msgReceived.getContent());
				}
			}
		}
		catch(ClassNotFoundException e){
			System.err.println("Error NotFoundException in the received client Thread : " + e);
		}
		catch(IOException e){
			if(!isInterrupted()) {
				System.err.println("Error IOE Exception in the received client Thread : " + e);
			}
			else {
				// Si le thread est interrompu, l'exception n'est pas une erreur
				System.out.println("Interrompu.");
			}
		}
	}
}



  
