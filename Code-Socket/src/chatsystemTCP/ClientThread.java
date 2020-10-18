package chatsystemTCP;

import java.io.*;
import java.net.*;

/**
 * ClientThread est la classe qui représente le thread et la socket (côté serveur) 
 * gérant la connexion avec le client.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class ClientThread extends Thread {
	
	// ----- Attributs de ClientThread -----

	/**
	 * Socket du client côté serveur.
	 */
	private Socket clientSocket;

	/**
	 * Stream d'envoi des messages.
	 */
	private ObjectInputStream socIn;

	/**
	 * Stream d'écoute/de réception des messages.
	 */
	private ObjectOutputStream socOut;
	
	// ----- Méthodes -----

	/**
	* Constructeur : Crée les stream d'écoute et d'envoi.
	* @param s Le socket client (côté serveur)
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
	* Getter.
	* @return Le socket client côté serveur.
	**/
	public Socket getClientSocket() {
		return this.clientSocket;
	}

	/**
	* Getter.
	* @return Le stream d'envoi.
	**/
	public ObjectOutputStream getObjectOutputStream() {
		return this.socOut;
	}

	/**
	* Getter.
	* @return Le stream de réception.
	**/
	public ObjectInputStream getObjectInputStream() {
		return this.socIn;
	}

 	/**
	* run method
	* Read the Message object sent by the Client and send it to all
	* of the Clients connected
	  **/
	  
	/**
	 * Méthode run du thread : reçoit un objet message envoyé par le client 
	 * et l'envoie à tous les clients (via le serveur).
	 */
	public void run() {
    	try {
    		while (true) {
    			Message msg = (Message)socIn.readObject();
				ServerMultithreaded.sendMessagesToClient(msg);
    		}
		} catch (EOFException | SocketException e) {
			// un client a été fermé sans se déconnecter
			ServerMultithreaded.removeClientConnected(this);
			return; // quitte le thread
		} catch (Exception e) {
        	System.err.println("Error in ClientThread : " + e); 
        }
    }
}