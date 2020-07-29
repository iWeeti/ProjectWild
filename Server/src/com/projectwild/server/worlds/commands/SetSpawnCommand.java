package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
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

        int oldX = (int) Math.round(world.getSpawnPosition().getX() / 32f);
        int oldY = (int) Math.round(world.getSpawnPosition().getY() / 32f);

        world.setBlock(oldX, oldY, 1, 0);
        if(oldY-1 > 1)
            world.setBlock(oldX, oldY-1, 1, 0);

        int x = (int) Math.round(client.getPlayer().getPosition().getX() / 32.0);
        int y = (int) Math.round(client.getPlayer().getPosition().getY() / 32.0);

        world.setBlock(x, y, 1, 24);
        world.setBlock(x, y-1, 1, 23);

        world.setSpawnPosition(new Vector2(x * 32, y * 32));

        ChatMessagePacket packet = new ChatMessagePacket("[GREEN]Success! [WHITE]Spawn Position Set");
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    @Override
    public String help() {
        return "Sets Spawn Position";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
