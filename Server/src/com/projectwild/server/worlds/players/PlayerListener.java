package com.projectwild.server.worlds.players;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.blocks.BlockTypes;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.ItemTypes;
import com.projectwild.shared.packets.clothing.EquipPacket;
import com.projectwild.shared.packets.player.PlayerAnimationPacket;
import com.projectwild.shared.packets.items.MoveItemSlotsPacket;
import com.projectwild.shared.packets.player.local.MovePacket;
import com.projectwild.shared.packets.world.InteractBlockPacket;
import com.projectwild.shared.utils.Vector2;

public class PlayerListener extends Listener {

    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof MovePacket) {
            MovePacket packet = (MovePacket) obj;
            Player player = WildServer.getClientHandler().getClientBySocket(connection.getID()).getPlayer();

            //TODO: check the pos to correct with server / update pos and speed
            //TODO: pathfinding and shit like that also check movement size n shit yknow

            player.updatePosition(packet.getNewPos(), false);
        }

        if(obj instanceof PlayerAnimationPacket) {
            PlayerAnimationPacket packet = (PlayerAnimationPacket) obj;
            Player player = WildServer.getClientHandler().getClientBySocket(connection.getID()).getPlayer();
            if(packet.getAnimationId() >= 0) {
                packet = new PlayerAnimationPacket(player.getClient().getUserId(), packet.getAnimationId());
                for(Player ply : player.getWorld().getPlayers()) {
                    if(player.getClient().getUserId() != ply.getClient().getUserId())
                        ply.getClient().sendTCP(packet);
                }
            }
        }

        if(obj instanceof InteractBlockPacket) {
            InteractBlockPacket packet = (InteractBlockPacket) obj;
            Player player = WildServer.getClientHandler().getClientBySocket(connection.getID()).getPlayer();

            if(!player.isOverride() && !player.getWorld().hasAccess(player.getClient()))
                return;

            // Check If There Are Any Players There
            boolean isColliding = false;
            for(Player ply : player.getWorld().getPlayers()) {
                Vector2[] corners = new Vector2[] {
                        new Vector2(ply.getPosition().getX() + 8, ply.getPosition().getY() + 26),
                        new Vector2(ply.getPosition().getX() + 24, ply.getPosition().getY() + 26),
                        new Vector2(ply.getPosition().getX() + 8, ply.getPosition().getY()),
                        new Vector2(ply.getPosition().getX() + 24, ply.getPosition().getY())
                };

                for(Vector2 v : corners) {
                    int blockX = (int) Math.floor(v.getX() / 32.0f);
                    int blockY = (int) Math.floor(v.getY() / 32.0f);

                    if(blockX == packet.getX() && blockY == packet.getY()) {
                        isColliding = true;
                        break;
                    }
                }
            }

            // checks world border
            if(packet.getY() < 0 || packet.getX() < 0 || packet.getZ() < 0)
                return;

            if(packet.getY() > player.getWorld().getBlocks().length)
                return;

            if(packet.getX() > player.getWorld().getBlocks()[0].length)
                return;

            if(packet.getZ() > 2)
                return;

            // prevents breaking unbreakable blocks
            if(!player.isOverride() && player.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()].getBlockPreset().getBlockType() == BlockTypes.UNBREAKABLE.getId())
                return;

            // Perform Interaction for placing
            if(player.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()].getBlockPreset().getId() == 0) {
                // Doing Inventory Stuff

                if(packet.getSlot() >= 8 || packet.getSlot() < 0)
                    return;

                ItemStack itemStack = player.getClient().getInventorySlot(packet.getSlot());
                if(itemStack == null)
                    return;

                if(itemStack.getItemPreset().getItemType() != ItemTypes.BLOCK.getId())
                    return;

                if(!player.isOverride() && isColliding) // TODO: check if the collision type is 1 so you can place blocks you can walkthrough on yourself and others.
                    return;

                player.getClient().setInventorySlot(packet.getSlot(), new ItemStack(itemStack.getItemPreset(), itemStack.getAmount()-1));

                // Placing Block
                player.getWorld().setBlock(packet.getX(), packet.getY(), packet.getZ(), itemStack.getItemPreset().getBlockId());
            }
            // Perform interaction for breaking
            else {
                // Doing Inventory Stuff
                int blockId = player.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()].getBlockPreset().getId();
                for(ItemPreset preset : ItemPreset.getItemPresets()) {
                    if(preset.getBlockId() == blockId)
                        player.getClient().changeItems(preset, 1);
                }

                player.getWorld().setBlock(packet.getX(), packet.getY(), packet.getZ(), 0);
            }
        }

        if(obj instanceof MoveItemSlotsPacket) {
            MoveItemSlotsPacket packet = (MoveItemSlotsPacket) obj;

            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());

            if(packet.getOriginSlot() < 0 || packet.getTargetSlot() < 0)
                return;

            if(packet.getOriginSlot() >= client.getInventory().length || packet.getTargetSlot() >= client.getInventory().length)
                return;

            ItemStack origin = client.getInventorySlot(packet.getOriginSlot());
            ItemStack target = client.getInventorySlot(packet.getTargetSlot());

            client.setInventorySlot(packet.getOriginSlot(), target);
            client.setInventorySlot(packet.getTargetSlot(), origin);
        }

        if(obj instanceof EquipPacket) {
            EquipPacket packet = (EquipPacket) obj;

            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());

            client.equip(packet.getInvSlot(), packet.getClothingSlot());
        }

    }

}
