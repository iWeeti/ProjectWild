package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.shared.packets.ChatMessagePacket;

import java.util.Arrays;

public class PrivateMessageCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(args.length < 2) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Missing Arguments");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        Client c = WildServer.getClientHandler().getClientByUsername(args[0]);
        if(c == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Couldn't Find Player");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for(String s : Arrays.copyOfRange(args, 1, args.length)) {
            builder.append(s);
            builder.append(" ");
        }

        ChatMessagePacket packet = new ChatMessagePacket(String.format("[PURPLE][PM] %s >> [WHITE] %s", client.getUsername(), builder.toString()));
        WildServer.getServer().sendToTCP(c.getSocket(), packet);

        packet = new ChatMessagePacket(String.format("[PURPLE][PM] Sent To %s >> [WHITE] %s", c.getUsername(), builder.toString()));
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    @Override
    public String help() {
        return "Private Message";
    }

}
