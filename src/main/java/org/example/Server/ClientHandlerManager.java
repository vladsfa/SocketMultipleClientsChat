package org.example.Server;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientHandlerManager {
    private final Set<ClientHandler> clientHandlers;
    private final Logger logger;

    public ClientHandlerManager() {
        this.clientHandlers = new HashSet<>();
        this.logger = new Logger(System.getProperty("user.dir") + "\\" + "log.txt");
    }

    public void add(ClientHandler clientHandler){
        this.clientHandlers.add(clientHandler);
        String name = clientHandler.getUsername();
        sendMessage("SERVER", name, "Вітаємо у чаті " + name + "!");
    }

    public void onMessageReceive(String receiverUsername, String msg){
        String[] parts = msg.split("-");
        String msgPart = parts[0];

        if (msg.contains("-username=")){
            int usernameFlagStartIndex = msg.indexOf("-username=");
            int equalsSignIndex = msg.indexOf("=", usernameFlagStartIndex);
            String usernameTo = msg.substring(equalsSignIndex + 1);

            sendMessage(receiverUsername, usernameTo, msgPart);
        }
        else{
            sendBroadcastMsg(receiverUsername, msgPart);
        }
    }

    private void sendBroadcastMsg(String from, String msg){
        for (ClientHandler clientHandler: clientHandlers){
            String to = clientHandler.getUsername();
            if (!to.equals(from)){
                clientHandler.sendMessage(getFormattedMsg(from, msg));
                logger.log(from, to, msg);
            }
        }
    }

    private void sendMessage(String fromUsername, String toUsername, String msg){
        Optional<ClientHandler> to = find(toUsername);

        if (to.isEmpty()){
            sendMessage("SERVER", fromUsername, "Користувача з таким іменем не існує");
            return;
        }

        to.get().sendMessage(getFormattedMsg(fromUsername, msg));
        logger.log(fromUsername, toUsername, msg);
    }

    private Optional<ClientHandler> find(String username){
        return this.clientHandlers.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst();
    }

    private String getFormattedMsg(String fromUsername, String msg){
        return fromUsername + ": " + msg;
    }

    public void close(){
        logger.close();
        for (ClientHandler clientHandler: clientHandlers){
            clientHandler.close();
        }
    }
}
