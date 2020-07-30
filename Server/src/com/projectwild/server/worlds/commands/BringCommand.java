package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class BringCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(!client.getPlayer().isOverride() && !world.hasAccess(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] You Don't Have Permission");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(args.length < 1) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Missing Arguments");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

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

        c.getPlayer().updatePosition(client.getPlayer().getPosition(), true);

        ChatMessagePacket packet = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Brought %s", c.getUsername()));
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    @Override
    public String help() {
        return "Bring Player To You";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
