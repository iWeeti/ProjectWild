package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.blocks.BlockTypes;

public class ClearCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        if(!client.getPlayer().isOverride() && world.getOwner() != client.getUserId()) {
            client.sendChatMessage("[RED]Failed![WHITE] You Don't Have Permission");
            return;
        }

        for(int y = 0; y < world.getHeight(); y++) {
            for(int x = 0; x < world.getWidth(); x++) {
                for(int z = 0; z < 2; z++) {
                    if(world.getBlocks()[y][x][z].getBlockPreset().getBlockType() != BlockTypes.UNBREAKABLE.getId())
                        world.setBlock(x, y, z, 0);
                }
            }
        }

        client.sendChatMessage("[GREEN]Success![WHITE] World Cleared");
    }

    @Override
    public String help() {
        return "Clears World";
    }

    @Override
    public boolean worldOwnerOnly() {
        return true;
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[0];
    }

}
