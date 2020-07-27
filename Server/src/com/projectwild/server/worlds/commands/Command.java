package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;

public interface Command {

    void execute(Client client, String[] args);

    String help();

}
