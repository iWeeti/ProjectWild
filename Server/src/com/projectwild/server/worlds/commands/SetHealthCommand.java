package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.player.local.UpdateHealthPacket;
import com.projectwild.shared.utils.Utils;

public class SetHealthCommand implements Command{

    @Override
    public void execute(Client client, World world, Object[] args) {
        Player player = (Player) args[0];
        int health = (int) args[1];

        health = Utils.clamp(450, -1, health);
        player.setHealth(health);
        client.sendChatMessage(String.format("[GREEN]Success! [WHITE]Set %s's health to %s", player.getClient().getUsername(), health));
    }

    @Override
    public String help() {
        return "Set health of a player.";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.PLAYER, CommandHandler.ArgType.INTEGER};
    }
}
