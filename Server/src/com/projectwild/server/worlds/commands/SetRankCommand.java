package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;

public class SetRankCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Client c = (Client) args[0];
        Rank rank = Rank.getRank((String) args[1]);

        c.setRank(rank);

        // Update Nametags
        UpdateNameTagPacket packet = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : c.getPlayer().getWorld().getPlayers()) {
            ply.getClient().sendTCP(packet);
        }

        client.sendChatMessage(String.format("[GREEN]Success! [WHITE] Set %s's Rank To %s", c.getUsername(), rank.getIdentifier()));
    }

    @Override
    public String help() {
        return "Sets A Players Rank";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.DEVELOPER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.CLIENT, CommandHandler.ArgType.STRING};
    }

}
