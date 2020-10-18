package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;
import chatsystemIHM.Window;

/**
 * ClientReceivingThread est la classe dédiée à la réception
 * des messages depuis le serveur.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class ClientReceivingThread extends Thread {

	// ----- Attribut du thread client de réception -----

	/**
	 * Stream qui permet de recevoir des objets (Message) depuis le serveur.
	 */
	private ObjectInputStream socIn;

	/**
	 * La fenêtre gérant l'affichage.
	 */
	private Window window;
	
	// ----- Méthodes -----

	/**
	 * Constructeur : Récupère le stream d'écoute et la fenêtre et 
	 * lance le thread.
	 * 
	 * @param in Stream d'écoute
	 * @param window Fenêtre gérant l'affichage
	 */
	ClientReceivingThread(ObjectInputStream in, Window window) {
		this.socIn = in;
		this.window = window;
		this.start();
	}
	  
	/**
	 * Méthode run du thread : Lit le stream d'objet et attend la réception d'un message. 
	 * Affiche ensuite le message dans la fenêtre du chat.
	 */
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