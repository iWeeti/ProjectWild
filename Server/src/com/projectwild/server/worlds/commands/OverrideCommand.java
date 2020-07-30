package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;

public class OverrideCommand implements Command {
    @Override
    public void execute(Client client, String[] args) {
        client.getPlayer().setOverride(!client.getPlayer().isOverride());
        Command.sendChatMessage(client, "[GREEN]Success! [WHITE]Override: %s.", client.getPlayer().isOverride() ? "on" : "off");
    }

    @Override
    public String help() {
        return "Toggle override.";
    }

    @Override
    public Rank rank() {
        return Rank.DEVELOPER;
    }
}
