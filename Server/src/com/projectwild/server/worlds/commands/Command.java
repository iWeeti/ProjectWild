package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;

public interface Command {

    void execute(Client client, World world, Object[] args);

    String help();

    boolean worldOwnerOnly();

    Rank rank();

    CommandHandler.ArgType[] arguments();

}
