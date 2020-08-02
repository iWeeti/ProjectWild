package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.blocks.BlockTypes;
import com.projectwild.server.worlds.blocks.types.SignBlock;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.world.UpdateBlockPacket;
import com.projectwild.shared.utils.Vector2;

public class SetTextCommand implements Command {
    @Override
    public void execute(Client client, World world, Object[] args) {
        if (!client.getPlayer().isOverride() && !world.hasAccess(client)){
            client.sendChatMessage("[RED]Fail! [WHITE]You don't have access in this world.");
            return;
        }

        Vector2 position = client.getPlayer().getPosition();
        Block block = world.pointCollisionBlock(position);
        client.sendChatMessage(block.getBlockPreset().getName());

        if (block.getBlockPreset().getBlockType() == BlockTypes.SIGN.getId()){
            SignBlock sign = (SignBlock) block;
            sign.setText((String) args[0]);
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket((int) position.getX(), (int) position.getY(), 1, sign.getBlockPreset().getId(), sign.serialize());
            for (Player p : world.getPlayers()){
                p.getClient().sendTCP(updateBlockPacket);
            }
            client.sendChatMessage("[GREEN]Success! [WHITE]Updated sign.");
        } else {
            client.sendChatMessage("[RED]Fail! [WHITE]Stand on a sign block to use this command.");
        }
    }

    @Override
    public String help() {
        return "Update a sign.";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.STRING_CONCAT};
    }
}
