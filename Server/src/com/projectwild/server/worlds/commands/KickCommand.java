package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;

public class KickCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Player player = (Player) args[0];
        world.destroyPlayer(player);

        client.sendChatMessage(String.format("[GREEN]Success! [WHITE]Kicked %s", player.getClient().getUsername()));
    }

    @Override
    public String help() {
        return "Kicks Specified Player";
    }

    @Override
    public boolean worldOwnerOnly() {
        return true;
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.PLAYER};
    }

}
