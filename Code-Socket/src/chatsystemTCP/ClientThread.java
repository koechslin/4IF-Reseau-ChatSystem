package chatsystemTCP;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
	
	// Attributs de ClientThread
	private Socket clientSocket;
	private ObjectInputStream socIn;
	private ObjectOutputStream socOut;
	
	// Méthodes

	/**
	* constructor
	* @param Socket the client socket
	**/
	ClientThread(Socket s) {
		this.clientSocket = s;
		try {
			socOut = new ObjectOutputStream(s.getOutputStream());
			socOut.flush();
			socIn = new ObjectInputStream(s.getInputStream());
		} catch (Exception e) {
			System.err.println("Error when creating ClientThread : " + e); 
		}
	}

	/**
	* getter
	* @return the variable 'clientSocket'
	**/
	public Socket getClientSocket() {
		return this.clientSocket;
	}

	/**
	* getter
	* @return the variable 'socOut'
	**/
	public ObjectOutputStream getObjectOutputStream() {
		return this.socOut;
	}

	/**
	* getter
	* @return the variable 'socIn'
	**/
	public ObjectInputStream getObjectInputStream() {
		return this.socIn;
	}

 	/**
	* run method
	* Read the Message object sent by the Client and send it to all
	* of the Clients connected
  	**/
	public void run() {
    	try {
    		while (true) {
    			Message msg = (Message)socIn.readObject();
				ServerMultithreaded.sendMessagesToClient(msg);
    		}
		} catch (EOFException | SocketException e) {
			// un client a été fermé sans se déconnecter
			ServerMultithreaded.removeClientConnected(this);
			return; // exit the thread
		} catch (Exception e) {
        	System.err.println("Error in ClientThread : " + e); 
        }
    }
}

  
