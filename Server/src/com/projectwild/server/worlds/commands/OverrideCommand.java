package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;

public class OverrideCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        client.getPlayer().setOverride(!client.getPlayer().isOverride());
        client.sendChatMessage("[GREEN]Success! [WHITE]Override: %s.", client.getPlayer().isOverride() ? "on" : "off");
    }

    @Override
    public String help() {
        return "Toggle override.";
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
        return new CommandHandler.ArgType[0];
    }

}
