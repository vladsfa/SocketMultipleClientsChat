package org.example.Client;

import java.io.IOException;
import java.net.Socket;

public class Main {
    private static final int port = 4200;
    private static final String name = "localhost";

    public static void main(String[] args) throws IOException {
        Client client = new Client(new Socket(name, port));
        client.start();
        client.listenMessage();
    }
}
