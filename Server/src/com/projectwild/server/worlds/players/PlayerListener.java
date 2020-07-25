package com.projectwild.server.worlds.players;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.server.WildServer;
import com.projectwild.server.worlds.blocks.types.StaticBlock;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.player.PlayerAnimationPacket;
import com.projectwild.shared.packets.player.local.MovePacket;
import com.projectwild.shared.packets.world.InteractBlockPacket;
import com.projectwild.shared.packets.world.UpdateBlockPacket;
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
                        WildServer.getServer().sendToTCP(ply.getClient().getSocket(), packet);
                }
            }
        }

        if(obj instanceof InteractBlockPacket) {
            InteractBlockPacket packet = (InteractBlockPacket) obj;
            Player player = WildServer.getClientHandler().getClientBySocket(connection.getID()).getPlayer();

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

            if(isColliding)
                return;

            // Perform Interaction
            if(player.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()].getBlockPreset().getId() == 0) {
                StaticBlock block = new StaticBlock(BlockPreset.getPreset(1));
                player.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()] = block;
                UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket(packet.getX(), packet.getY(), packet.getZ(), block.getBlockPreset().getId(), block.serialize());
                for(Player ply : player.getWorld().getPlayers()) {
                    WildServer.getServer().sendToTCP(ply.getClient().getSocket(), updateBlockPacket);
                }
            } else {
                StaticBlock block = new StaticBlock(BlockPreset.getPreset(0));
                player.getWorld().getBlocks()[packet.getY()][packet.getX()][packet.getZ()] = block;
                UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket(packet.getX(), packet.getY(), packet.getZ(), block.getBlockPreset().getId(), block.serialize());
                for(Player ply : player.getWorld().getPlayers()) {
                    WildServer.getServer().sendToTCP(ply.getClient().getSocket(), updateBlockPacket);
                }
            }
        }
    }

}
