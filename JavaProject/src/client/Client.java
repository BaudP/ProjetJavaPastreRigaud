package client;

import shared.*;


import java.net.Socket;
import java.lang.String;
import java.lang.ClassNotFoundException;
import java.util.Scanner;
import java.io.*;

public class Client implements Runnable{

    /**
     * The server address (localhost)
     */
    public static final String SERVER_HOST = null;

    /**
     * The listening port
     */
    public static final int SERVER_PORT = 3000;

    /**
     * The client socket
     */
    private Socket socket;

    /**
     * The client output stream
     */
    private ObjectOutputStream oos;

    /**
     * The client input stream
     */
    private ObjectInputStream ois;

    /**
     * Read a user's information
     */
    private Scanner sc = new Scanner(System.in);

    /**
     * The success of server authentication
     */
    private boolean ClientConnexionSucessfull = false;

    public Client() throws IOException {
        this.socket = new Socket(SERVER_HOST, SERVER_PORT);
        this.oos = new ObjectOutputStream(this.socket.getOutputStream());
        this.oos.flush();
        this.ois = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            // Demande d'authentification tant que le client n'est pas connecté.
            while (!this.ClientConnexionSucessfull) {
            	 System.out.println("Voulez-vous cr�er un compte");
            	 System.out.println("1 : Oui");
            	 System.out.println("2 : Non");
            	 int newAccount = this.sc.nextInt();
            	 connexion(newAccount);
            }
            // L'authentification est validé, le client peut recevoir et envoyer des messages.

            // Permet de recevoir des messages, quand un message doit être reçu.
            Thread t3 = new Thread(new Reception(ois));
            // Permet d'envoyer des messages indépendamment de la récéption des messages.
            Thread t4 = new Thread(new Emission(oos));
            t3.start();
            t4.start();
        } catch (IOException e) {
            System.err.println("Le serveur distant s'est déconnecté !");
        }
    }

    //
    public void connexion(int newAccount) throws IOException {
    	boolean accountExist= true; // par defaut un compte existe.
    	if(newAccount==1) { // Voulez-vous créer un compte ? Réponse : 1 (Oui)
    		accountExist = false; // Donc le compte est inexistant.
    	}
    	// On entre un login et mot de passe (Compte existant ou non).
        System.out.println("Login : ");
        String login = this.sc.next();
        System.out.println("Password : ");
        String password = this.sc.next();
        // Création d'une requête d'authentification.
        AuthentificationRequest ClientConnection = new AuthentificationRequest(login, password, accountExist);
        oos.writeObject(ClientConnection); // Envoye de la requête sur le serveur.

        try {
            if (ois.readObject().equals("connecte")) { // Si le client reçoit une validation d'authentification.
                System.out.println("Je suis connecté ");
                this.ClientConnexionSucessfull = true; // Le client est connecté.
            } else {
                System.err.println("Vos informations sont incorrectes ");
                // test
                this.ClientConnexionSucessfull = true; // Le client est connecté.
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("impossible de vous connecter");
        }
    }
}
