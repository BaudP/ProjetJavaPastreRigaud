package server;

import java.io.IOException;

public class MainServer {

    public static void main (String args[]) {
        try {
            Server server = new Server();
            System.out.println("Lancement du server");
            server.run();
        } catch (IOException e){
            System.out.println("Serveur arrêté");
            //e.printStackTrace();
        }

    }
}
