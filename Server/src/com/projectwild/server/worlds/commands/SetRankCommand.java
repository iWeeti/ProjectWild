package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;

public class SetRankCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(args.length < 2) {
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

        Rank rank = Rank.getRank(args[1]);
        c.setRank(rank);
        UpdateNameTagPacket packet = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : c.getPlayer().getWorld().getPlayers()) {
            ply.getClient().sendTCP(packet);
        }

        ChatMessagePacket messagePacket = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE] Set %s's Rank To %s", c.getUsername(), rank.getIdentifier()));
        client.sendTCP(messagePacket);
    }

    @Override
    public String help() {
        return "Sets A Players Rank";
    }

    @Override
    public Rank rank() {
        return Rank.DEVELOPER;
    }

}
