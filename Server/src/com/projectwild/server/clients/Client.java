package com.projectwild.server.clients;

import com.projectwild.server.worlds.players.Player;

public class Client {

    private int userId, socket;
    private String username;

    private Player player;
    
    public Client(int socket, int userId, String username) {
        this.socket = socket;
        this.userId = userId;
        this.username = username;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSocket() {
        return socket;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
}
