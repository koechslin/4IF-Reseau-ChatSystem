package chatsystemTCP;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public  class ServerMultithreaded {

    // Variables globales de ServerMultithreaded
	static ServerSocket listenSocket;
	static List<ClientThread> clientsConnected;
	static List<Message> historiqueMsg;
	static File fMsg;
	static FileWriter fWriter;

	// Méthodes

	/**
	 * main method
	* Launch the server, open a listen socket 
	* and launch the ClientThread when a client 
	* connects to the server
	* @param Server port
	* @param Messages file
	**/
    public static void main(String args[]){        
  		if (args.length != 2) {
        	System.out.println("Usage: java ServerMultithreaded <Server port> <Messages file>");
        	System.exit(1);
		}
		historiqueMsg = new ArrayList<Message>();
		fMsg = new File(args[1]);
		try {
			if(fMsg.exists()) {
				// lire le fichier
				Scanner fReader = new Scanner(fMsg);
				while(fReader.hasNext()) {
					String line = fReader.nextLine();
					String[] lineSplit = line.split("\\|");
					Message msgFile = new Message(lineSplit[0], lineSplit[1], 2);
					historiqueMsg.add(msgFile);
				}
				fReader.close();
			}
			else {
				fMsg.createNewFile();
			}
			fWriter = new FileWriter(fMsg.getAbsolutePath(), true);
		} catch (Exception e) {
			System.out.println("Error when opening/creating file : " + e);
		}
		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
			clientsConnected = new ArrayList<ClientThread>();
			System.out.println("Server is ready !");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("Connexion from : " + clientSocket.getInetAddress());
				ClientThread ct = new ClientThread(clientSocket);
				addClientConnected(ct);
				ct.start();

				Thread.sleep(50);

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

		if(msg.getType() != 1) {
			// message qui n'est pas de type information
			historiqueMsg.add(msg);
			try {
				fWriter.append(msg.getContent() + "|" + msg.getPseudo() + "\n");
				fWriter.flush();
			} catch (Exception e) {
				System.out.println("Error when writing to file : " + e);
			}
		}
		
		for(ClientThread c: clientsConnected){
			try {
				c.getObjectOutputStream().writeObject(msg);
			}
			catch (SocketException e) {
				// un client a été fermé sans se déconnecter
				removeClientConnected(c);
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