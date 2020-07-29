package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.shared.packets.ChatMessagePacket;

public class OnlineCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        ChatMessagePacket packet = new ChatMessagePacket(String.format("There Are %s Player(s) Online", WildServer.getClientHandler().getClients().size()));
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    @Override
    public String help() {
        return "Shows Amount Of Players Online";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
