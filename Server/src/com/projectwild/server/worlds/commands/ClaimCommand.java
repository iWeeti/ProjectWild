package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;

public class ClaimCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();

        if(world.claimWorld(client)) {
            // Message
            ChatMessagePacket packet = new ChatMessagePacket("[GREEN]Success! [WHITE]World Claimed");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);

            // Updating Access & Nametag
            UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(false);
            UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(client.getUserId(), client.getPlayer().getNametag());
            for(Player ply : world.getPlayers()) {
                if(ply.getClient().getUserId() != client.getUserId())
                    WildServer.getServer().sendToTCP(ply.getClient().getSocket(), hasAccessPacket);
                WildServer.getServer().sendToTCP(ply.getClient().getSocket(), nameTagPacket);
            }
        } else {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Claim World");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
        }
    }

    @Override
    public String help() {
        return "Claims World";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

}
