package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;

public class NameCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        String name = (String) args[0];
        if(name.isEmpty()) {
            client.resetUsername();
            client.sendChatMessage("[GREEN]Success! [WHITE]Reset Username");
        } else {
            if(client.setUsername(name)) {
                client.sendChatMessage("[GREEN]Success! [WHITE]Set Username To %s", name);
            } else {
                client.sendChatMessage("[RED]Failed! [WHITE]Someones With That Username Is Logged In", name);
            }
        }
    }

    @Override
    public String help() {
        return "Change your username";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.STRING_CONCAT};
    }
}
