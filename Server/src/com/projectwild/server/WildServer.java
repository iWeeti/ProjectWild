package com.projectwild.server;

import com.esotericsoftware.kryonet.Server;
import com.projectwild.server.clients.AuthListener;
import com.projectwild.server.clients.ClientHandler;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.WorldHandler;
import com.projectwild.server.worlds.WorldListener;
import com.projectwild.server.worlds.commands.CommandHandler;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.server.worlds.players.PlayerListener;
import com.projectwild.shared.PacketRegistry;

import java.io.IOException;

public class WildServer {

    private static DatabaseController databaseController;
    private static Server server;
    
    private static ClientHandler clientHandler;
    private static WorldHandler worldHandler;
    private static CommandHandler commandHandler;

    private static UpdateLoop updateLoop;

    public static void main(String[] args) throws IOException {
        databaseController = new DatabaseController("data/projectwild.db");
        clientHandler = new ClientHandler();
        worldHandler = new WorldHandler();
        commandHandler = new CommandHandler();
        
        server = new Server(10000000, 10000000);
        PacketRegistry.register(server.getKryo());
        server.addListener(new AuthListener());
        server.addListener(new WorldListener());
        server.addListener(new PlayerListener());
        server.start();
        server.bind(7707, 7707);

        updateLoop = new UpdateLoop();
        updateLoop.setCallback(() -> {
            // Update Blocks & Players
            for(World world : worldHandler.getWorlds()) {
                for(int x = 0; x < world.getWidth(); x++) {
                    for(int y = 0; y < world.getHeight(); y++) {
                        for(int z = 0; z < 2; z++) {
                            world.getBlocks()[y][x][z].update();
                        }
                    }
                }

                for(Player ply : world.getPlayers()) {
                    ply.checkCollision();
                }
            }

            // Performance
            System.out.print(String.format("\r[PERF] Delta >> %s\t\tMemory Usage >> %sMB", updateLoop.getDelta(), Math.round((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0)));
        });
        updateLoop.start();
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

    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }

}
