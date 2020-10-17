package chatsystemUDP;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.net.DatagramPacket;
import java.util.Scanner;
import java.io.*;


public class ChatServer {

    // Variables globales du serveur
    static List<String> historiqueMsg;
    static FileWriter fWriter;
    static File fMsg;

    //Méthodes

    /**
     * main method
    * Connect the server to the group
    * save all the messages and send the history when asking
    **/
    public static void main(String[] args) {
        if (args.length != 3) {
        	System.out.println("Usage: java ChatServer <Group adress> <Group port> <NameFile>");
        	System.exit(1);
          }
          historiqueMsg=new ArrayList<String>();

          //Récupère l'historique depuis le fichier
          try {
            File fMsg = new File(args[2]);
            try {
                if(fMsg.exists()) {
                    // lire le fichier
                    Scanner fReader = new Scanner(fMsg);
                    while(fReader.hasNext()) {
                        String line = fReader.nextLine();
                        historiqueMsg.add(line);
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
            //Création du socket multicast et rejoint le groupe
            MulticastSocket socket = new MulticastSocket(new Integer(args[1]).intValue());
            InetAddress groupAddr = InetAddress.getByName(args[0]);
            socket.joinGroup(groupAddr);
            System.out.println("Server is ready !");

            while(true) 
            {
                byte[] buf = new byte[1024];
                DatagramPacket msgRecv = new DatagramPacket(buf, buf.length);
                socket.receive(msgRecv);
				String msg = new String(msgRecv.getData(), "UTF-8");
                msg = msg.replaceAll("\u0000.*", ""); // enlève les caractères null

                String typeMessage=msg.substring(msg.length()-1);
                msg=msg.substring(0, msg.length()-1);
                System.out.println("Type message recu : "+typeMessage);
                
                if(typeMessage.equals("1")){
                    //Message normal à ajouter à l'historique
                    historiqueMsg.add(msg);
                    try {
                        fWriter.append(msg+"\n");
                        fWriter.flush();
                    } catch (Exception e) {
                        System.out.println("Error when writing to file : " + e);
                    }
                } 
                
                else
                {
                    if(typeMessage.equals("2"))
                    {
                        //Envoie de l'historique des messages
                        try {
                            String msgToSendString="";
                            for(String m:historiqueMsg){
                                msgToSendString=msgToSendString+m+"\n";
                            }

                            if(msgToSendString.length()>1){
                                msgToSendString=msgToSendString.substring(0,msgToSendString.length()-1)+"3";
                            }
                            else{
                                msgToSendString="3";
                            }
                            
                            DatagramPacket msgToSend = new DatagramPacket(msgToSendString.getBytes(), msgToSendString.length(), groupAddr,new Integer(args[1]).intValue());
                            socket.send(msgToSend);
                        
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }  
            }
          } catch (Exception e) {
              System.out.println("Error in ChatServer : " + e);
          }
    }
}
