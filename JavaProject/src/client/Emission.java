package client;

import shared.Response;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.lang.String;

public class Emission implements Runnable {

    /**
     * The client output stream
     */
    private ObjectOutputStream oos;

    /**
     * Read a user's information
     */
    private Scanner sc = new Scanner(System.in);

    /**
     * The constructor
     * @param oos The client output stream
     */
    public Emission(ObjectOutputStream oos) {
        this.oos = oos;
    }

    @Override
    public void run() {
        String msg; // Le message a envoyer
        while(true) {
            try {
                msg = this.sc.next();
                Response send_message = new Response(msg); // Création de la requête d'envoie.
                this.oos.writeObject(send_message);
                this.oos.flush();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("message non envoyé");
            }
        }
    }
}
