package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.player.local.UpdateHealthPacket;

public class SetHealthCommand implements Command{

    @Override
    public void execute(Client client, String[] args) {
        if (args.length < 1){
            Command.sendChatMessage(client, "[RED]Fail! [WHITE]Include health argument.");
            return;
        }

        Client toSet = client;
        if (args.length >= 2){
            for (Player player : client.getPlayer().getWorld().getPlayers())
                if (player.getNametag().toLowerCase().startsWith(args[1].toLowerCase()))
                    toSet = player.getClient();
        }

        toSet.getPlayer().setHealth(Integer.parseInt(args[0]));
        Command.sendChatMessage(client, "[GREEN]Success! [WHITE]");
    }

    @Override
    public String help() {
        return "Set health of a player.";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }
}
