package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;

public class TeleportCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Player player = (Player) args[0];

        world.playSoundForAll("whoosh_1");
        client.getPlayer().updatePosition(player.getPosition(), true);
        client.sendChatMessage(String.format("[GREEN]Success! [WHITE]Teleported you to %s.", player.getClient().getUsername()));
    }

    @Override
    public String help() {
        return "Teleports you to another player";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.PLAYER};
    }
}
