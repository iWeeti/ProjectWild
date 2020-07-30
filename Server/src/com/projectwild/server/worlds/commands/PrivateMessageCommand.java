package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.shared.packets.ChatMessagePacket;

import java.util.Arrays;

public class PrivateMessageCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(args.length < 2) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Missing Arguments");
            client.sendTCP(packet);
            return;
        }

        Client c = WildServer.getClientHandler().getClientByUsername(args[0]);
        if(c == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Couldn't Find Player");
            client.sendTCP(packet);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for(String s : Arrays.copyOfRange(args, 1, args.length)) {
            builder.append(s);
            builder.append(" ");
        }

        ChatMessagePacket packet = new ChatMessagePacket(String.format("[PURPLE][PM] %s >> [WHITE] %s", client.getUsername(), builder.toString()));
        c.sendTCP(packet);

        packet = new ChatMessagePacket(String.format("[PURPLE][PM] Sent To %s >> [WHITE] %s", c.getUsername(), builder.toString()));
        client.sendTCP(packet);
    }

    @Override
    public String help() {
        return "Private Message";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
