package com.projectwild.game.ingame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.blocks.BlockTypes;
import com.projectwild.game.ingame.player.Player;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.items.ChangeInventoryItemPacket;
import com.projectwild.shared.packets.items.LoadInventoryPacket;
import com.projectwild.shared.packets.player.*;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;
import com.projectwild.shared.packets.player.local.UpdateSpeedMultiplierPacket;
import com.projectwild.shared.packets.world.UpdateBlockPacket;
import com.projectwild.shared.packets.world.UpdateNetworkedVariablePacket;
import com.projectwild.shared.packets.world.WorldDataPacket;

import java.lang.reflect.InvocationTargetException;

public class WorldListener extends Listener {

    private WorldState worldState;

    public WorldListener(WorldState worldState) {
        this.worldState = worldState;
    }

    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof WorldDataPacket) {
            WorldDataPacket packet = (WorldDataPacket) obj;
            worldState.setWorld(new World(packet));
        }

        if(obj instanceof PlayerSpawnPacket) {
            PlayerSpawnPacket packet = (PlayerSpawnPacket) obj;

            Player player;
            if(!packet.isLocal()) {
                player = worldState.getWorld().createPlayer(packet.getUserId(), packet.getUsername());
            } else {
                player = worldState.getWorld().createLocalPlayer(packet.getUserId(), packet.getUsername());
            }
            player.getPosition().set(packet.getPosition());
        }

        if(obj instanceof PlayerRemovePacket) {
            PlayerRemovePacket packet = (PlayerRemovePacket) obj;
            if(worldState.getWorld().getLocalPlayer() != null) {
                if(worldState.getWorld().getLocalPlayer().getUserId() == packet.getUserId())
                    WildGame.changeState(new WorldSelectionState());
            }
            worldState.getWorld().removePlayer(packet.getUserId());
        }

        if(obj instanceof UpdatePositionPacket) {
            UpdatePositionPacket packet = (UpdatePositionPacket) obj;
            Player player = worldState.getWorld().getPlayer(packet.getUserId());
            if(player != null)
                player.getPosition().set(packet.getPosition());
        }

        if(obj instanceof UpdateSpeedMultiplierPacket) {
            UpdateSpeedMultiplierPacket packet = (UpdateSpeedMultiplierPacket) obj;
            if(worldState.getWorld() == null)
                return;

            if(worldState.getWorld().getLocalPlayer() == null)
                return;

            worldState.getWorld().getLocalPlayer().setSpeedMultiplier(packet.getSpeedMultiplier());
        }

        if(obj instanceof UpdateHasAccessPacket) {
            UpdateHasAccessPacket packet = (UpdateHasAccessPacket) obj;
            if(worldState.getWorld() == null)
                return;

            if(worldState.getWorld().getLocalPlayer() == null)
                return;

            worldState.getWorld().getLocalPlayer().setHasAccess(packet.hasAccess());
        }

        if(obj instanceof UpdateNameTagPacket) {
            UpdateNameTagPacket packet = (UpdateNameTagPacket) obj;

            if(worldState.getWorld() == null)
                return;

            Player ply = worldState.getWorld().getPlayer(packet.getUserId());

            if(ply == null)
                return;

            ply.setNametag(packet.getNametag());
        }

        if(obj instanceof UpdateNetworkedVariablePacket) {
            UpdateNetworkedVariablePacket packet = (UpdateNetworkedVariablePacket) obj;
            if(worldState.getWorld() == null)
                return;

            worldState.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()].setNWVar(packet.getKey(), packet.getValue());
        }

        if(obj instanceof PlayerAnimationPacket) {
            PlayerAnimationPacket packet = (PlayerAnimationPacket) obj;
            Player player = worldState.getWorld().getPlayer(packet.getUserId());
            if(player != null)
                player.changeAnimation(packet.getAnimationId());
        }

        if(obj instanceof UpdateBlockPacket) {
            UpdateBlockPacket packet = (UpdateBlockPacket) obj;
            try {
                BlockPreset preset = BlockPreset.getPreset(packet.getBlockPresetId());
                Block block = BlockTypes.getBlockClass(preset.getBlockType()).getConstructor(BlockPreset.class, byte[].class).newInstance(preset, packet.getExtraData());
                worldState.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()] = block;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if(obj instanceof LoadInventoryPacket) {
            LoadInventoryPacket packet = (LoadInventoryPacket) obj;
            worldState.getInventoryHandler().updateInventory(packet.getInventory());
        }

        if(obj instanceof ChangeInventoryItemPacket) {
            ChangeInventoryItemPacket packet = (ChangeInventoryItemPacket) obj;
            worldState.getInventoryHandler().changeItems(packet.getSlot(), packet.getItemStack());
        }

        if(obj instanceof ChatMessagePacket) {
            ChatMessagePacket packet = (ChatMessagePacket) obj;
            worldState.getChatHandler().addMessage(packet.getMessage());
        }
    }

}
