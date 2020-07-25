package com.projectwild.server.clients;

import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
    
    private CopyOnWriteArrayList<Client> clients;
    
    public ClientHandler() {
        clients = new CopyOnWriteArrayList<>();
    }
    
    public void loginClient(Client client) {
        clients.add(client);
    }
    
    public void disconnectClient(Client client) {
        clients.remove(client);
    }
    
    public Client getClientBySocket(int socket){
        for(Client c : clients){
            if(c.getSocket() == socket)
                return c;
        }
        return null;
    }
    
    public Client getClientByUserId(int userId){
        for(Client c : clients){
            if(c.getUserId() == userId)
                return c;
        }
        return null;
    }

}
