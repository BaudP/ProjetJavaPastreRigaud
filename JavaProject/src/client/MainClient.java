package client;

import java.io.IOException;

public class MainClient {

    public static void main (String args[]) throws IOException {
        //Thread cli = new Thread( new Client());
        Client cli = new Client();
        System.out.println("lancement du client");
        cli.run();
        //cli.start();
    }
}

