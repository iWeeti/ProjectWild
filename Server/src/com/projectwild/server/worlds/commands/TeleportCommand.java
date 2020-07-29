package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;

public class TeleportCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : args)
            stringBuilder.append(s);
        Client to = null;
        for (Player player : client.getPlayer().getWorld().getPlayers())
            if (player.getNametag().toLowerCase().startsWith(stringBuilder.toString()))
                to = player.getClient();
        if (to == null) {
            WildServer.getServer().sendToTCP(client.getSocket(), new ChatMessagePacket(String.format("[RED]Fail! [WHITE]Couldn't find the player %s.", stringBuilder.toString())));
            return;
        }
        client.getPlayer().getPosition().set(to.getPlayer().getPosition());
        WildServer.getServer().sendToTCP(client.getSocket(), new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Teleported you to %s.", stringBuilder.toString())));
    }

    @Override
    public String help() {
        return "Teleports you to another player.";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }
}
