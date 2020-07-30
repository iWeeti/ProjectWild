package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;

public class PrivateMessageCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Client c = (Client) args[0];
        String message = (String) args[1];

        c.sendChatMessage(String.format("[PURPLE][PM] %s >> [WHITE] %s", client.getUsername(), message));
        client.sendChatMessage(String.format("[PURPLE][PM] Sent To %s >> [WHITE] %s", c.getUsername(), message));
    }

    @Override
    public String help() {
        return "Private Message";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.CLIENT, CommandHandler.ArgType.STRING_CONCAT};
    }

}
