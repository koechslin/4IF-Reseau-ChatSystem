package chatsystemTCP;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.ArrayList;

public  class ServerMultithreaded {

    // Variables globales de ServerMultithreaded
	static ServerSocket listenSocket;
	static List<ClientThread> clientsConnected;
	static List<Message> historiqueMsg;

	// Méthodes 

	/**
	 * main method
	* Launch the server, open a listen socket 
	* and launch the ClientThread when a client 
	* connects to the server
	* @param Server port
	**/
    public static void main(String args[]){        
  		if (args.length != 1) {
        	System.out.println("Usage: java ServerMultithreaded <Server port>");
        	System.exit(1);
  		}
		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
			clientsConnected = new ArrayList<ClientThread>();
			historiqueMsg=new ArrayList<Message>();
			System.out.println("Server is ready !");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("Connexion from : " + clientSocket.getInetAddress());
				ClientThread ct = new ClientThread(clientSocket);
				addClientConnected(ct);
				ct.start();

				for(Message m : historiqueMsg) {
					// Envoie l'historique des messages au client qui vient de se connecter
					ct.getObjectOutputStream().writeObject(m);
				}
			}
        } catch (Exception e) {
            System.err.println("Error with the server : " + e);
        }
	}

	/**
	* Send a message to all the clients and add it to the history
	* @param msg the message to redistribute to all the clients
	**/
	public static synchronized void sendMessagesToClient(Message msg) {
		if(msg.getType() != 1) historiqueMsg.add(msg); // message qui n'est pas de type information

		for(ClientThread c: clientsConnected){
			try {
				c.getObjectOutputStream().writeObject(msg);
			}
			catch (Exception e) {
				System.err.println("Error with the server when redistributing the message : " + msg + " to all the clients : " + e);
			}
		}
	}

	/**
	* Add a new ClientThread to the list of clients connected
	* @param ct the ClientThread to add
	**/
	public static synchronized void addClientConnected(ClientThread ct) {
		clientsConnected.add(ct);
	}

	/**
	* Remove a disconnected ClientThread from the list of clients connected
	* @param ct the ClientThread to remove
	**/
	public static synchronized void removeClientConnected(ClientThread ct) {
		clientsConnected.remove(ct);
	}
}