package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class BroadcastCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        WildServer.getServer().sendToAllTCP(new ChatMessagePacket(String.format("[YELLOW]Broadcast: [WHITE]%s", args[0])));
    }

    @Override
    public String help() {
        return "Broadcast a message.";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.DEVELOPER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.STRING_CONCAT};
    }
}
