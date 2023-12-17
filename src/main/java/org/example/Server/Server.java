package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private final ServerSocket serverSocket;
    private final ClientHandlerManager clientHandlerManager;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clientHandlerManager = new ClientHandlerManager();
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!serverSocket.isClosed()){
                        Socket socket = serverSocket.accept();
                        new Thread(new ClientHandler(socket, clientHandlerManager)).start();
                    }
                } catch (IOException e) {
                    close();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void close(){
        try {
            clientHandlerManager.close();
            if (serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
