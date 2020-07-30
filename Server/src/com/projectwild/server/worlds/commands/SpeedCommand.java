package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.player.local.UpdateSpeedMultiplierPacket;

public class SpeedCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        float speedMultiplier = (float) args[0];

        client.sendTCP(new UpdateSpeedMultiplierPacket(speedMultiplier));
        client.sendChatMessage(String.format("[GREEN]Success![WHITE] Set Speed Multipier To %s", speedMultiplier));
    }

    @Override
    public String help() {
        return "Changes Your Speed";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.FLOAT};
    }

}
