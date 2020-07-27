package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class TrustCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(world.getOwner() != client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]You Don't Have Permission");
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

        world.addTrusted(c);

        ChatMessagePacket packet = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Trusted %s", c.getUsername()));
        WildServer.getServer().sendToTCP(client.getSocket(), packet);

        packet = new ChatMessagePacket(String.format("[GREEN]Trusted! [WHITE]You Are Now Trusted In %s", world.getName()));
        WildServer.getServer().sendToTCP(c.getSocket(), packet);
        return;
    }

    @Override
    public String help() {
        return "Allows You To Trust Players";
    }

}
