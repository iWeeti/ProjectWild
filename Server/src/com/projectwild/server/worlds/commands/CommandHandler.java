package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

import java.util.Collection;
import java.util.HashMap;

public class CommandHandler {

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
        commands.put("bring", new BringCommand());
        commands.put("kick", new KickCommand());
        commands.put("setrank", new SetRankCommand());
        commands.put("online", new OnlineCommand());
        commands.put("teleport", new TeleportCommand());
        commands.put("bc", new BroadcastCommand());
        commands.put("world", new WorldCommand());
        commands.put("override", new OverrideCommand());
        commands.put("noclip", new NoclipCommand());
        commands.put("sethealth", new SetHealthCommand());
    }

    public void executeCommand(String command, Client client, String[] args) {
        if(commands.containsKey(command.toLowerCase())) {
            Command cmd = commands.get(command.toLowerCase());
            if(cmd.rank().getPower() <= client.getRank().getPower()) {
                cmd.execute(client, args);
            } else {
                ChatMessagePacket packet = new ChatMessagePacket("[RED]You Don't Have Permission To Use This Command");
                client.sendTCP(packet);
            }
        } else {
            ChatMessagePacket packet = new ChatMessagePacket(String.format("[RED]Unknown Command: %s", command.toLowerCase()));
            client.sendTCP(packet);
        }
    }

    public Collection<String> getCommandNames() {
        return commands.keySet();
    }

    public Collection<Command> getCommandClasses() {
        return commands.values();
    }

}
