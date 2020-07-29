package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;

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

        if(c.getUserId() == client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]You Cannot Trust Yourself");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(world.hasAccess(c)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]This Player Is Already Trusted");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        // Adding Trusted And Sending messages
        world.addTrusted(c);
        ChatMessagePacket packet = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Trusted %s", c.getUsername()));
        WildServer.getServer().sendToTCP(client.getSocket(), packet);

        packet = new ChatMessagePacket(String.format("[GREEN]Trusted! [WHITE]You Are Now Trusted In %s", world.getName()));
        WildServer.getServer().sendToTCP(c.getSocket(), packet);

        // Setting Local Player Access
        UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(true);
        WildServer.getServer().sendToTCP(c.getSocket(), hasAccessPacket);

        // Updating Nametag
        UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : world.getPlayers()) {
            WildServer.getServer().sendToTCP(ply.getClient().getSocket(), nameTagPacket);
        }
        return;
    }

    @Override
    public String help() {
        return "Allows You To Trust Players";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
