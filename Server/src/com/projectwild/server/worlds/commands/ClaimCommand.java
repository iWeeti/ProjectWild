package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class ClaimCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(world.claimWorld(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[GREEN]Success! [WHITE]World Claimed");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
        } else {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Claim World");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
        }
    }

    @Override
    public String help() {
        return "Claims World";
    }

}
