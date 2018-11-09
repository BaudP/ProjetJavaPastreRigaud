package client;

import shared.*;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Reception implements Runnable {

    /**
     * The client output stream
     */
    private ObjectInputStream ois;

    public Reception(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void run() {
        while(true) {
            try {
                Response message = ( Response ) this.ois.readObject();
                System.out.println(message.toString());
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
                System.out.println("message non recu");
            }
        }
    }
}
