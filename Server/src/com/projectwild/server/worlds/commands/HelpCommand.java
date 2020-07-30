package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class HelpCommand implements Command{

    @Override
    public void execute(Client client, World world, Object[] args) {
        StringBuilder builder = new StringBuilder();

        String[] names = WildServer.getCommandHandler().getCommandNames().toArray(String[]::new);
        Command[] commands = WildServer.getCommandHandler().getCommandClasses().toArray(Command[]::new);

        for(int i = 0; i < names.length; i++) {
            if(commands[i].rank().getPower() > client.getRank().getPower())
                continue;

            builder.append("[GREEN]/");
            builder.append(names[i].substring(0, 1).toUpperCase());
            builder.append(names[i].substring(1));
            builder.append(": [WHITE]");
            builder.append(commands[i].help());

            if(i != names.length-1)
                builder.append("\n");
        }

        ChatMessagePacket packet = new ChatMessagePacket(builder.toString());
        client.sendTCP(packet);
    }

    @Override
    public String help() {
        return "Shows All Commands";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[0];
    }

}
