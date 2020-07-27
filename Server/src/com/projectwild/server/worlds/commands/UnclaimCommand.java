package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class UnclaimCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(world.getOwner() != client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]You Don't Have Permission");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(world.claimWorld(null)) {
            ChatMessagePacket packet = new ChatMessagePacket("[GREEN]Success! [WHITE]World Unclaimed");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
        }
    }

    @Override
    public String help() {
        return "Unclaims World";
    }

}
