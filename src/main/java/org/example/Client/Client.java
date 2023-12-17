package org.example.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(Socket socket) {
        try{
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            close();
            e.printStackTrace();
        }
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                print("Введіть своє ім'я:");
                while (socket.isConnected()){
                    String msg = scanner.nextLine();
                    if (!msg.trim().isEmpty()){
                        out.println(msg);
                    }
                }
            }
        }).start();
    }

    public void listenMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()){
                        String msg = in.readLine();
                        print(msg);
                    }
                }catch (IOException e){
                    close();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void print(String msg){
        System.out.println(msg);
    }

    private void close(){
        try{
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
}
