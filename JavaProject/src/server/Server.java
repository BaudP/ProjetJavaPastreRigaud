package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server implements Runnable {

    /**
     * The server socket
     */
    private ServerSocket server;

    /**
     * The client number in a topic
     */
    public int nbClients = 0;

    /**
     * The number of client in a topic
     */
    private int numClient=0; // contiendra le numéro de client géré par l'authentification, plus facile à la suppression

    /**
     * The list of output streams from the server to the clients
     */
    private Vector tabClients = new Vector();

    /**
     * The listening port
     */
    private static final int SERVER_PORT = 3000;

    /**
     * The constructor
     */
    public Server()throws IOException {
        this.server = new ServerSocket(SERVER_PORT);
    }

    @Override
    public void run() {
        try {
            System.out.println("Attente de connexion ...");
            while(true){
                Socket client = server.accept();
                System.out.println("Connexion accept�");

                // Authentification d'un nouveau client.
                // Thread AuthentificationThread = new Thread(new Authentification(client, this.tabClients, this.nbClients));
                // AuthentificationThread.start();
                Authentification AuthClient= new Authentification(client, this.tabClients, this.nbClients);
                AuthClient.run();
            }
        } catch (IOException e){
            //e.printStackTrace();
            System.out.println("Connexion impossible");
        }
    }


}
