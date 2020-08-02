package com.projectwild.server.clients;

import com.projectwild.server.WildServer;

import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
    
    private CopyOnWriteArrayList<Client> clients;
    
    public ClientHandler() {
        clients = new CopyOnWriteArrayList<>();
    }
    
    public void loginClient(Client client) {
        // In Case A Mod Changed Their Name
        Client c = WildServer.getClientHandler().getClientByUsername(client.getUsername());
        if(c != null) {
            c.resetUsername();
            c.sendChatMessage("[RED]Your Username Was Reset");
        }

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

    public Client getClientByUsername(String username){
        for(Client c : clients){
            if(c.getUsername().toLowerCase().equals(username.toLowerCase()))
                return c;
        }
        return null;
    }

    public CopyOnWriteArrayList<Client> getClients() {
        return clients;
    }

}
