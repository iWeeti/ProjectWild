package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;

public class TeleportCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        Client c = WildServer.getClientHandler().getClientByUsername(args[0]);
        if(c == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(c.getPlayer() == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(!c.getPlayer().getWorld().getName().equals(world.getName())) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        client.getPlayer().updatePosition(c.getPlayer().getPosition(), true);
        WildServer.getServer().sendToTCP(client.getSocket(), new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Teleported you to %s.", c.getUsername())));
    }

    @Override
    public String help() {
        return "Teleports you to another player";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }
}
