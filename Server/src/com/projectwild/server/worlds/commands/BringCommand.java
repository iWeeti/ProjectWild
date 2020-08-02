package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;

public class BringCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Player ply = (Player) args[0];
        ply.updatePosition(client.getPlayer().getPosition(), true);
        world.playSoundForAll("whoosh_1");
        client.sendChatMessage(String.format("[GREEN]Success! [WHITE]Brought %s", ply.getClient().getUsername()));
    }

    @Override
    public String help() {
        return "Bring Player To You";
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
