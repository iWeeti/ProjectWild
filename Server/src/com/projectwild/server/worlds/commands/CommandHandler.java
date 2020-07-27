package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.shared.packets.ChatMessagePacket;

import java.util.Collection;
import java.util.HashMap;

public class CommandHandler {

    public static final String[] admins = {"ImWuX", "Nullrien"};

    public static boolean isAdmin(Client client) {
        for(String s : admins) {
            if(s.toLowerCase().equals(client.getUsername().toLowerCase()))
                return true;
        }
        return false;
    }

    private HashMap<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();
        commands.put("help", new HelpCommand());
        commands.put("clear", new ClearCommand());
        commands.put("setspawn", new SetSpawnCommand());
        commands.put("claim", new ClaimCommand());
        commands.put("unclaim", new UnclaimCommand());
        commands.put("give", new GiveCommand());
        commands.put("clearinv", new ClearInventoryCommand());
        commands.put("items", new ItemsCommand());
        commands.put("trust", new TrustCommand());
        commands.put("untrust", new UntrustCommand());
        commands.put("pm", new PrivateMessageCommand());
        commands.put("speed", new SpeedCommand());
    }

    public void executeCommand(String command, Client client, String[] args) {
        if(commands.containsKey(command.toLowerCase())) {
            commands.get(command.toLowerCase()).execute(client, args);
        } else {
            ChatMessagePacket packet = new ChatMessagePacket(String.format("[RED]Unknown Command: %s", command.toLowerCase()));
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
        }
    }

    public Collection<String> getCommandNames() {
        return commands.keySet();
    }

    public Collection<Command> getCommandClasses() {
        return commands.values();
    }

}
