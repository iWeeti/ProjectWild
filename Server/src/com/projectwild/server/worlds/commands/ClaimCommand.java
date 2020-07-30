package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;

public class ClaimCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {

        if(world.claimWorld(client)) {
            // Message
            client.sendChatMessage("[GREEN]Success! [WHITE]World Claimed");

            // Updating Access & Nametag
            UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(false);
            UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(client.getUserId(), client.getPlayer().getNametag());
            for(Player ply : world.getPlayers()) {
                if(ply.getClient().getUserId() != client.getUserId())
                    ply.getClient().sendTCP(hasAccessPacket);
                ply.getClient().sendTCP(nameTagPacket);
            }
        } else {
            client.sendChatMessage("[RED]Failed! [WHITE]Couldn't Claim World");
        }
    }

    @Override
    public String help() {
        return "Claims World";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[0];
    }

}
