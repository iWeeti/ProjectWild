package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;

public class SetRankCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(!CommandHandler.isDev(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Developer Only");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(args.length < 2) {
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

        c.setRank(args[1]);
        UpdateNameTagPacket packet = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : c.getPlayer().getWorld().getPlayers()) {
            WildServer.getServer().sendToTCP(ply.getClient().getSocket(), packet);
        }
    }

    @Override
    public String help() {
        return "Sets A Players Rank";
    }

}
