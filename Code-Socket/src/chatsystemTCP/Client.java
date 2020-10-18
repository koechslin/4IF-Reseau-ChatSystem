package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;
import chatsystemTCP.ClientReceivingThread;
import chatsystemIHM.Window;

/**
 * Client est la classe qui va gérer la connexion côté client et 
 * qui va permettre d'envoyer et de recevoir les messages.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class Client {
  
  // ----- Variables globales de Client -----

  /**
   * Socket qui va se connecter au serveur.
   */
  private Socket socket;

  /**
   * Stream qui permet d'envoyer des objets (Message) au serveur.
   */
  private ObjectOutputStream socOut;

  /**
   * Stream qui permet de recevoir des objets (Message) depuis le serveur.
   */
  private ObjectInputStream socIn;

  /**
   * Thread dédié à la réception des messages depuis le serveur.
   */
  private ClientReceivingThread RT;

  /**
   * Pseudo du client connecté.
   */
  private String pseudo;

  /**
   * Booléen indiquant si le client est connecté ou non.
   */
  private boolean isConnected = false;

  // ----- Méthodes -----

  /**
   * Méthode qui permet de se connecter au serveur et de lancer le thread 
   * de réception des messages.
   * 
   * @param host Adresse du serveur
   * @param port Port du serveur
   * @param pseudo Pseudo du client
   * @param window Fenêtre gérant l'affichage
   * @throws UnknownHostException
   * @throws IOException
   */
  public void connect(String host, int port, String pseudo, Window window) throws UnknownHostException, IOException {
    this.pseudo = pseudo;
    this.socket = new Socket(host, port);
    this.socOut = new ObjectOutputStream(this.socket.getOutputStream());
    this.socIn = new ObjectInputStream(this.socket.getInputStream());
    this.RT = new ClientReceivingThread(this.socIn, window);
    
    // message d'information de connexion
		Message msgInit = new Message(this.pseudo + " vient de se connecter au chat.", "Serveur", 1);
		try {
			Thread.sleep(100);
			this.socOut.writeObject(msgInit);
		} catch (Exception e) {
			System.err.println("Error when sending init message : " + e);
    }
    
    this.isConnected = true;
  }

  /**
   * Permet d'envoyer un message au serveur.
   * @param content Le contenu du message
   * @throws IOException
   */
  public void sendMsg(String content) throws IOException {
		Message msg = new Message(content, this.pseudo, 2);
		this.socOut.writeObject(msg);
	}

  /**
   * Permet de déconnecter le client (en fermant la socket).
   * @throws Exception
   */
  public void disconnect() throws Exception {
    this.RT.interrupt();
    this.socOut.close();
    this.socIn.close();
    this.socket.close();
    this.isConnected = false;
  }

  /**
   * Getter.
   * @return Le thread de réception des messages.
   */
  public ClientReceivingThread getClientReceivingThread() {
    return this.RT;
  }

  /**
   * Getter.
   * @return Le booléen indiquant l'état de connexion du client.
   */
  public boolean getIsConnected() {
    return this.isConnected;
  }
}
