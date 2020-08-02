package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;

public class HideRankCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        client.setHideRank(!client.isHideRank());
        client.sendChatMessage("[GREEN]Success! [WHITE]Set Hide Rank To: %s", client.isHideRank());
    }

    @Override
    public String help() {
        return "Hides Your Rank";
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
        return new CommandHandler.ArgType[0];
    }

}
