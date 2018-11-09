package server;

import shared.AuthentificationRequest;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.lang.String;

public class Authentification implements Runnable {

    /**
     * The socket client
     */
    private Socket client;

    /**
     * The output stream from server to client
     */
    private ObjectOutputStream oos = null;

    /**
     * The input stream server
     */
    private ObjectInputStream ois = null;

    /**
     * The login and password client
     */
    private String login, pass;

    /**
     * The sucess of authentification client
     */
    private boolean authentificationSuccessfull = false;

    /**
     * The client handler thread
     */
    private Thread ClientHandlerThread;
    /**
     * The number of client connected and number of a client
     */
    private int nbClients, numClient;

    /**
     * The client connected list
     */
    private Vector tabClients;

    /**
     * The constructor
     * @param client The client socket create in Server
     * @param nbClients The number of client in the topic
     * @param tabClients The list of output stream
     */
    public Authentification(Socket client, Vector tabClients, int nbClients) {
        this.client = client;
        this.tabClients = tabClients;
        this.nbClients = nbClients;
    }

    /**
     * Main function of Authentification class
     */
    public void run() {
        System.out.println("Authentification en cours");
        boolean accountExist; //** Compte existant.**
        
        try {
            this.ois = new ObjectInputStream(this.client.getInputStream());
            this.oos = new ObjectOutputStream(this.client.getOutputStream());
            while(!this.authentificationSuccessfull){
                //** Reception de la requÃªte d'authentification du client.**
                AuthentificationRequest clienteAuthentificate = (AuthentificationRequest) ois.readObject();
                this.login = clienteAuthentificate.getUsername();
                this.pass = clienteAuthentificate.getPassword();
                accountExist = clienteAuthentificate.getAccountExist();

                if(!accountExist) { //** Le compte n'existe pas.**
                	createAccount();  //** MÃ©thode : Creation de compte
                	System.out.println("Votre compte a bien été créé");
                }
                
                if(isValid(this.login, this.pass)){ //** VÃ©rification des authentifiants.**
                    this.oos.writeObject("connecte"); //** Envoie de la validation d'authentification au client
                    System.out.println(login +" vient de se connecter ");
                    this.oos.flush();
                    this.authentificationSuccessfull = true;
                }
                else {
                    this.oos.writeObject("erreur"); //** Authentification inccorrecte : Envoie d'un message d'erreur.**
                    this.oos.flush();
                    // test
                    
                }
            }
            System.out.println("New thread ClientHandler");
            // On ajoute Ã  la liste des flux de sortie le flux de sortie du nouveau client.
            // On retourne un numero de client qui servira Ã  supprimer le client de la liste
            this.numClient = addClient(this.oos);
            //** On lance le gestionnaire de client
            ClientHandlerThread = new Thread(
                    new ClientHandler(this.client, this.numClient, this.login, this.ois, this.tabClients, this.nbClients));
            ClientHandlerThread.start();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(login+" ne répond pas !");
            e.printStackTrace();
        }
    }

    /**
     * Methode : VÃ©rifie que le compte est prÃ©sent dans la liste de compte.**
     * @param login The authentication username
     * @param pass The authentication password
     * @return True if the login and pass are in the mdpfile.txt, else return False
     */
    private static boolean isValid(String login, String pass) {
        boolean connexion = false;
        try {
            try (Scanner sc = new Scanner(new File("mdpfile.txt"))) {
                while (sc.hasNext()) {
                    if (sc.nextLine().equals(login + " " + pass)) {
                        connexion = true;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier n'existe pas !");
        }
        return connexion;
    }

    /**
     * Function to create an accompte
     * insert login and pass variable of  in the mdpfile.txt
     */
    private void createAccount() {
         File FichierMdp = new File("./mdpfile.txt");
         FileReader fr_identifiant;

            try {
                    fr_identifiant = new FileReader(FichierMdp);
                    BufferedReader br_identifiant = new BufferedReader(fr_identifiant);
                   FileWriter fw =new FileWriter(FichierMdp,true);
                   BufferedWriter bw = new BufferedWriter(fw);
                   bw.write("\n");
                bw.write(this.login+" "+ this.pass);
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }

    /**
     * Methode : ajoute un nouveau client dans la liste
     * @param oos The client output stream
     * @return The number of client output stream
     */
    synchronized public int addClient(ObjectOutputStream oos) {
        this.nbClients++; // un client en plus ! ouaaaih
        this.tabClients.addElement(oos); // on ajoute le nouveau flux de sortie au tableau
        return this.tabClients.size()-1; // on retourne le numÃ©ro du client ajoutÃ© (size-1)
    }
}

