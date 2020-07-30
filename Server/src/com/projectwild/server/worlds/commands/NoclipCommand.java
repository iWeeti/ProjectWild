package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;

public class NoclipCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        client.getPlayer().setNoclip(!client.getPlayer().isNoclip());
        Command.sendChatMessage(client, "[GREEN]Success! [WHITE]Noclip: %s.", client.getPlayer().isNoclip() ? "on" : "off");
    }

    @Override
    public String help() {
        return "Let's you fly/walk through blocks";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

}
