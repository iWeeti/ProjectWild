package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;

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
        commands.put("setinventorysize", new SetInventorySizeCommand());
    }

    public void executeCommand(String command, Client client, String[] args) {
        if(commands.containsKey(command.toLowerCase())) {
            Command cmd = commands.get(command.toLowerCase());
            if(cmd.rank().getPower() <= client.getRank().getPower()) {

                World world = client.getPlayer().getWorld();
                if(cmd.worldOwnerOnly() && !client.getPlayer().isOverride() && !world.hasAccess(client)) {
                    client.sendChatMessage("[RED]Failed![WHITE] You Don't Have Permission To Do That Here");
                    return;
                }

                // Check Length
                if(args.length < cmd.arguments().length) {
                    client.sendChatMessage("[RED]Missing Arguments");
                    return;
                }

                // Convert Into Correct Types
                Object[] convertedArgs = new Object[cmd.arguments().length];
                for(int i = 0; i < cmd.arguments().length; i++) {
                    boolean stopLoop = false;
                    switch(cmd.arguments()[i]) {
                        case STRING:
                            convertedArgs[i] = args[i];
                            break;
                        case INTEGER:
                            try {
                                convertedArgs[i] = Integer.parseInt(args[i]);
                            } catch(NumberFormatException e) {
                                client.sendChatMessage(String.format("[RED]Failed![WHITE] %s Is Not A Valid Integer", args[i]));
                                return;
                            }
                            break;
                        case FLOAT:
                            try {
                                convertedArgs[i] = Float.parseFloat(args[i]);
                            } catch(NumberFormatException e) {
                                client.sendChatMessage(String.format("[RED]Failed![WHITE] %s Is Not A Valid Float", args[i]));
                                return;
                            }
                            break;
                        case CLIENT:
                            convertedArgs[i] = WildServer.getClientHandler().getClientByUsername(args[i]);
                            if(convertedArgs[i] == null) {
                                client.sendChatMessage(String.format("[RED]Failed![WHITE] Player %s Is Not Online", args[i]));
                                return;
                            }
                            break;
                        case PLAYER:
                            Client c = WildServer.getClientHandler().getClientByUsername(args[i]);
                            if(c == null) {
                                client.sendChatMessage(String.format("[RED]Failed![WHITE] Player %s Is Not In Your World", args[i]));
                                return;
                            }

                            if(c.getPlayer() == null) {
                                client.sendChatMessage(String.format("[RED]Failed![WHITE] Player %s Is Not In Your World", args[i]));
                                return;
                            }

                            if(!c.getPlayer().getWorld().getName().equals(client.getPlayer().getWorld().getName())) {
                                client.sendChatMessage(String.format("[RED]Failed![WHITE] Player %s Is Not In Your World", args[i]));
                                return;
                            }
                            convertedArgs[i] = c.getPlayer();
                            break;
                        case STRING_CONCAT:
                            StringBuilder builder = new StringBuilder();
                            for(int j = i; j < args.length; j++) {
                                builder.append(args[j]);
                                builder.append(" ");
                            }
                            convertedArgs[i] = builder.toString();
                            stopLoop = true;
                            break;
                    }

                    if(stopLoop)
                        break;
                }
                cmd.execute(client, world, convertedArgs);
            } else {
                client.sendChatMessage("[RED]You Don't Have Permission To Use This Command");
            }
        } else {
            client.sendChatMessage(String.format("[RED]Unknown Command: %s", command.toLowerCase()));
        }
    }

    public Collection<String> getCommandNames() {
        return commands.keySet();
    }

    public Collection<Command> getCommandClasses() {
        return commands.values();
    }

    public enum ArgType {
        STRING,
        STRING_CONCAT,
        FLOAT,
        INTEGER,
        PLAYER,
        CLIENT;
    }

}
