package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static final int port = 4200;

    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(port));
        server.start();
    }
}