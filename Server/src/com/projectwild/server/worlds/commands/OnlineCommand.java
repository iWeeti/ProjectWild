package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;

public class OnlineCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        client.sendChatMessage(String.format("There Are %s Player(s) Online", WildServer.getClientHandler().getClients().size()));
    }

    @Override
    public String help() {
        return "Shows Amount Of Players Online";
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
