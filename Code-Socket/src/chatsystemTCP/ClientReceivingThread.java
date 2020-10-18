package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;
import chatsystemIHM.Window;

public class ClientReceivingThread extends Thread {

	// Attribut du thread client de réception
	private ObjectInputStream  socIn;
	private Window window;
	
	/**
	* constructor
	* @param in the input object stream
  	**/
	ClientReceivingThread(ObjectInputStream in, Window window) {
		this.socIn = in;
		this.window = window;
		this.start();
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
				this.window.receiveMsg(msgReceived);
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



  
