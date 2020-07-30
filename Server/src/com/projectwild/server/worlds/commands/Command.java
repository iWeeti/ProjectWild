package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.shared.packets.ChatMessagePacket;

public interface Command {

    void execute(Client client, String[] args);

    String help();

    Rank rank();

    static void sendChatMessage(Client client, String message) {
        client.sendTCP(new ChatMessagePacket(message));
    }

    static void sendChatMessage(Client client, String message, Object... args) {
        message = String.format(message, args);
        client.sendTCP(new ChatMessagePacket(message));
    }

}
