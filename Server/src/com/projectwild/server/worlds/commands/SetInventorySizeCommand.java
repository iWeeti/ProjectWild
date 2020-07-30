package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;

public class SetInventorySizeCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Client c = (Client) args[0];
        c.setInventorySize((int) args[1]);
    }

    @Override
    public String help() {
        return "Sets Players Inventory Size";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.CLIENT, CommandHandler.ArgType.INTEGER};
    }

}
