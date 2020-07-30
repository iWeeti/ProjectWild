package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;

public class KickCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();
        Player player = client.getPlayer();

        if (!player.isOverride() && !world.hasAccess(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] You Don't Have Permission");
            client.sendTCP(packet);
            return;
        }

        if(args.length < 1) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Missing Arguments");
            client.sendTCP(packet);
            return;
        }

        Client c = WildServer.getClientHandler().getClientByUsername(args[0]);
        if(c == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            client.sendTCP(packet);
            return;
        }

        if(c.getPlayer() == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            client.sendTCP(packet);
            return;
        }

        if(!c.getPlayer().getWorld().getName().equals(world.getName())) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            client.sendTCP(packet);
            return;
        }

        world.destroyPlayer(c.getPlayer());

        ChatMessagePacket packet = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Kicked %s", c.getUsername()));
        client.sendTCP(packet);
    }

    @Override
    public String help() {
        return "Kicks Specified Player";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
