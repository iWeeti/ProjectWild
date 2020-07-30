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

        if(!client.getPlayer().isOverride() && world.getOwner() != client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]You Don't Have Permission");
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

        if (!client.getPlayer().isOverride() && c.getUserId() == client.getUserId()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]You Cannot Trust Yourself");
            client.sendTCP(packet);
            return;
        }

        if(world.hasAccess(c)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]This Player Is Already Trusted");
            client.sendTCP(packet);
            return;
        }

        // Adding Trusted And Sending messages
        world.addTrusted(c);
        ChatMessagePacket packet = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Trusted %s", c.getUsername()));
        client.sendTCP(packet);

        packet = new ChatMessagePacket(String.format("[GREEN]Trusted! [WHITE]You Are Now Trusted In %s", world.getName()));
        c.sendTCP(packet);

        // Setting Local Player Access
        UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(true);
        c.sendTCP(hasAccessPacket);

        // Updating Nametag
        UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : world.getPlayers()) {
            ply.getClient().sendTCP(nameTagPacket);
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
