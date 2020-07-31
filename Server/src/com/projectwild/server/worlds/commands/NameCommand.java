package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;

public class NameCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {

        for (Player player: world.getPlayers()) {
            player.getClient().sendTCP(new UpdateNameTagPacket(client.getUserId(), ((String) args[0])));
        }
    }

    @Override
    public String help() {
        return "Change your nametag";
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
