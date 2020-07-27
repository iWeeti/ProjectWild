package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

public class ClearCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(world.getOwner() != client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] You Don't Have Permission");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        for(int y = 0; y < world.getHeight(); y++) {
            for(int x = 0; x < world.getWidth(); x++) {
                for(int z = 0; z < 2; z++) {
                    world.setBlock(x, y, z, 0);
                }
            }
        }

        ChatMessagePacket packet = new ChatMessagePacket("[GREEN]Success![WHITE] World Cleared");
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    @Override
    public String help() {
        return "Clears World";
    }

}
