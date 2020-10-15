package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;

public class ClientSendingThread extends Thread {

	// Attributs du thread client de lecture 
	private BufferedReader stdIn;
	private ObjectOutputStream  socOut;
	private String pseudo;

	// Méthodes

	/**
	* constructor
	* @param in the input bufferReader
	* @param socOut the output object stream
	* @param pseudo the client's pseudo
	**/
	ClientSendingThread(BufferedReader in,ObjectOutputStream socOut, String pseudo) {
		this.stdIn = in;
		this.socOut=socOut;
		this.pseudo=pseudo;
	}

	/**
	* run method
	* Read the console of the client, wait for a message
	* and send it to the server
  	**/
	public void run() {
		// message d'information de connexion
		Message msgInit = new Message(this.pseudo + " vient de se connecter au chat.", "Serveur", 1);
		try {
			this.socOut.writeObject(msgInit);
		} catch (Exception e) {
			System.err.println("Error when sending init message : " + e);
		}

		String line;
        while (true) {
			try{
				line = stdIn.readLine();
				if (line.equals(".")) break;
				Message msg = new Message(line, pseudo, 2);
				socOut.writeObject(msg);
			}
			catch (IOException e) {
				System.err.println("Error when sending a message in ThreadReadClient");
			}
		}
		Client.disconnect();
	}
}

  
