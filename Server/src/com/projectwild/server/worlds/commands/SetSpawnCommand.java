package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.utils.Vector2;

public class SetSpawnCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(world.getOwner() != client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] You Don't Have Permission");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        int x = (int) Math.round(client.getPlayer().getPosition().getX() / 32.0) * 32;
        int y = (int) Math.round(client.getPlayer().getPosition().getY() / 32.0) * 32;

        world.setSpawnPosition(new Vector2(x, y));

        ChatMessagePacket packet = new ChatMessagePacket("[GREEN]Success! [WHITE]Spawn Position Set");
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    @Override
    public String help() {
        return "Sets Spawn Position";
    }

}
