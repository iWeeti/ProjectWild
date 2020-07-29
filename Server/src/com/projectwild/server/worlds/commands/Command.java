package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;

public interface Command {

    void execute(Client client, String[] args);

    String help();

    Rank rank();

}
