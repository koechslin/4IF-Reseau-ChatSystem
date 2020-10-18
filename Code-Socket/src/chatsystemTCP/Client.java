package chatsystemTCP;

import java.io.*;
import java.net.*;
import chatsystemTCP.Message;
import chatsystemTCP.ClientReceivingThread;
import chatsystemIHM.Window;

public class Client {
  
  // Variables globales de Client
  private Socket socket;
  private ObjectOutputStream socOut;
  private ObjectInputStream socIn;
  private ClientReceivingThread RT; // Thread qui va permettre de recevoir les messages du serveur
  private String pseudo;
  private boolean isConnected = false;

  // Méthodes

  public void connect(String host, int port, String pseudo, Window window) throws UnknownHostException, IOException {
    this.pseudo = pseudo;
    this.socket = new Socket(host, port);
    this.socOut = new ObjectOutputStream(this.socket.getOutputStream());
    this.socIn = new ObjectInputStream(this.socket.getInputStream());
    this.RT = new ClientReceivingThread(this.socIn, window);
    
    // message d'information de connexion
		Message msgInit = new Message(this.pseudo + " vient de se connecter au chat.", "Serveur", 1);
		try {
			Thread.sleep(70);
			this.socOut.writeObject(msgInit);
		} catch (Exception e) {
			System.err.println("Error when sending init message : " + e);
    }
    
    this.isConnected = true;
  }

  public void sendMsg(String content) throws IOException {
		Message msg = new Message(content, this.pseudo, 2);
		this.socOut.writeObject(msg);
	}

  /**
  * disconnect method
  * Interrupt the reading and writing threads and close the socket
  **/
  public void disconnect() throws Exception {
    this.RT.interrupt();
    this.socOut.close();
    this.socIn.close();
    this.socket.close();
    this.isConnected = false;
  }

  public ClientReceivingThread getClientReceivingThread() {
    return this.RT;
  }

  public boolean getIsConnected() {
    return this.isConnected;
  }
}


