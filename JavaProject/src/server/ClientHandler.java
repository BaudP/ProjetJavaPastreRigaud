package server;

import shared.Response;

import java.net.Socket;
import java.io.*;
import java.util.Vector;

public class ClientHandler implements Runnable{
    /**
     * The socket client
     */
    private Socket client;
    /**
     * The client number in list client
     */
    private int numClient;

    /**
     * The number of client in topic
     */
    private int nbClients;

    /**
     * The client login
     */
    private String login;

    /**
     * The list of client output stream
     */
    private Vector tabClients;

    /**
     * The server input stream
     */
    private ObjectInputStream ois;

    /**
     * DataBase of chat
     */
    File MonFichier = new File("./MonFichier.txt");

    /**
     * The Constructor
     * @param client The socket client
     * @param numClient Not used
     * @param login The client login of message receive
     * @param ois The server input stream
     * @param tabClients The list of client output stream
     */
    public ClientHandler(Socket client,int numClient, String login, ObjectInputStream ois, Vector tabClients, int nbClients) {
        this.client = client;
        this.numClient = numClient;
        this.login = login;
        this.ois = ois;
        this.tabClients = tabClients;
        this.nbClients = nbClients;
    }

    @Override
    public void run() {
        System.out.println("run HandlerClient");
        Response recept_msg = null;
        Response send_msg;
        while(true) {
            try {
                recept_msg = ( Response ) this.ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
                System.err.println("message non recu");
                try {
                    delClient();
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // message affiché sur le serveur
            String msg = this.login + " : " + recept_msg.toString();
            System.out.println(msg);
            saveFile(msg);
            
            send_msg = new Response(msg);
            try {
                sendAll(send_msg);
            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println("message non envoy�");
                try {
                    delClient();
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //error = true;
            }
        }
    }
    private void saveFile(String msg) {
    	FileReader fr_identifiant;
		try {
			fr_identifiant = new FileReader(MonFichier);
			BufferedReader br_identifiant = new BufferedReader(fr_identifiant);
			FileWriter fw =new FileWriter(MonFichier,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(msg+"\n");
			if (bw != null)
			    bw.close();
			if (fw != null)
			    fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.err.println("Impossible d'ecrire dans le fichier");
		}
       
    }

    /**
     * Methode : send the message to all clients
     * @param send_msg the message to send
     * @throws IOException
     */
    synchronized public void sendAll(Response send_msg) throws IOException {
        ObjectOutputStream oos; // declaration d'une variable permettant l'envoi de texte vers le client
        for (int i = 0; i < this.tabClients.size(); i++) // parcours de la liste des clients connectés
        {
            oos = (ObjectOutputStream ) this.tabClients.elementAt(i); // Choix du client pour recevoir le message
            if (oos != null) // sécurité, l'élément ne doit pas être vide.
            {
                oos.writeObject(send_msg); // Envoie du message au client.
                oos.flush();
            }
        }
    }

    /**
     * Methode : delete client via numClient;
     */
    synchronized public void delClient() {
        this.nbClients--; // suppression d'un client
        if (this.tabClients.elementAt(this.numClient) != null) { // l'élément existe ...
            this.tabClients.removeElementAt(this.numClient); // suppression de flux de sortie
        }
    }
}
