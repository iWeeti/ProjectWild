package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.shared.packets.ChatMessagePacket;

public class BroadcastCommand implements Command{
    @Override
    public void execute(Client client, String[] args) {
        StringBuilder text = new StringBuilder();
        for (String s : args)
            text.append(s);
        WildServer.getServer().sendToAllTCP(new ChatMessagePacket(String.format("[YELLOW]Broadcast: [WHITE]%s", text.toString())));
    }

    @Override
    public String help() {
        return "Broadcast a message.";
    }

    @Override
    public Rank rank() {
        return Rank.DEVELOPER;
    }
}
