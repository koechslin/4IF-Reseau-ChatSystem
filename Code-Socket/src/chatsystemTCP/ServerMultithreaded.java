package chatsystemTCP;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * ServerMultithreaded est la classe qui représente le serveur. C'est 
 * elle qui gère la connexion avec les clients, et l'envoi et la  réception 
 * des messages.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class ServerMultithreaded {

    // ----- Variables globales de ServerMultithreaded ------
	
	/**
	 * Socket d'écoute pour que les clients puissent se connecter.
	 */
	static ServerSocket listenSocket;

	/**
	 * Liste des clients connectés.
	 */
	static List<ClientThread> clientsConnected;

	/**
	 * Historique des messages envoyés.
	 */
	static List<Message> historiqueMsg;

	/**
	 * Fichier dans lequel est stocké l'historique des messages.
	 */
	static File fMsg;

	/**
	 * Variable permettant d'écrire dans le fichier de l'historique.
	 */
	static FileWriter fWriter;

	// ----- Méthodes -----

	/**
	 *  method
	* Launch the server, open a listen socket 
	* and launch the ClientThread when a client 
	* connects to the server
	* @param Server port
	* @param Messages file
	**/

	/**
	 * Méthode main : Lance le serveur et ouvre la socket d'écoute. 
	 * Lorsqu'un client se connnecte, crée un objet ClientThread associé 
	 * au client et lui envoie l'historique des messages.
	 * @param args Contient en premier le port du serveur et en deuxième le fichier de l'historique des messages.
	 */
    public static void main(String args[]){        
  		if (args.length != 2) {
        	System.out.println("Usage: java ServerMultithreaded <Server port> <Messages file>");
        	System.exit(1);
		}
		historiqueMsg = new ArrayList<Message>();
		fMsg = new File(args[1]);
		try {
			if(fMsg.exists()) {
				// lit le fichier
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
				// crée le fichier
				fMsg.createNewFile();
			}
			fWriter = new FileWriter(fMsg.getAbsolutePath(), true);
		} catch (Exception e) {
			System.out.println("Error when opening/creating file : " + e);
		}
		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); // port
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
	 * Envoie un message à tous les clients et l'ajoute à l'historique.
	 * @param msg Le message à envoyer.
	 */
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
	 * Ajoute un nouveau ClientThread aux clients connectés.
	 * @param ct Le ClientThread à ajouter.
	 */
	public static synchronized void addClientConnected(ClientThread ct) {
		clientsConnected.add(ct);
	}

	/**
	 * Supprime un ClientThread des clients connectés.
	 * @param ct Le ClientThread a supprimer
	 */
	public static synchronized void removeClientConnected(ClientThread ct) {
		clientsConnected.remove(ct);
	}
}