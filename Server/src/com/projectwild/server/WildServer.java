package com.projectwild.server;

import com.esotericsoftware.kryonet.Server;
import com.projectwild.server.clients.AuthListener;
import com.projectwild.server.clients.ClientHandler;
import com.projectwild.server.worlds.WorldHandler;
import com.projectwild.server.worlds.WorldListener;
import com.projectwild.server.worlds.players.PlayerListener;
import com.projectwild.shared.PacketRegistry;

import java.io.IOException;

public class WildServer {
    
    private static DatabaseController databaseController;
    private static Server server;
    
    private static ClientHandler clientHandler;
    private static WorldHandler worldHandler;
    
    public static void main(String[] args) throws IOException {
        databaseController = new DatabaseController("data/projectwild.db");
        clientHandler = new ClientHandler();
        worldHandler = new WorldHandler();
        
        server = new Server(10000000, 10000000);
        PacketRegistry.register(server.getKryo());
        server.addListener(new AuthListener());
        server.addListener(new WorldListener());
        server.addListener(new PlayerListener());
        server.start();
        server.bind(7707, 7707);
    }
    
    public static DatabaseController getDatabaseController() {
        return databaseController;
    }
    
    public static Server getServer() {
        return server;
    }
    
    public static ClientHandler getClientHandler() {
        return clientHandler;
    }
    
    public static WorldHandler getWorldHandler() {
        return worldHandler;
    }
    
}
