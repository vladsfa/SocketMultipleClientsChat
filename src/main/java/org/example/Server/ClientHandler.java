package org.example.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private ClientHandlerManager clientHandlerManager;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket, ClientHandlerManager clientHandlerManager) {
        try {
            this.socket = socket;
            this.clientHandlerManager = clientHandlerManager;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);

            this.username = in.readLine();

            this.clientHandlerManager.add(this);
        }
        catch (IOException e){
            this.close();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (this.socket.isConnected()){
                String msg = this.in.readLine();
                this.clientHandlerManager.onMessageReceive(username, msg);
            }
        } catch (IOException e){
            this.close();
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String msg){
        this.out.println(msg);
    }

    public synchronized void close(){
        try {
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}
