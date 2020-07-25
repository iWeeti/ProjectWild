package com.projectwild.server.worlds;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.world.RequestWorldPacket;
import com.projectwild.shared.packets.world.RequestWorldResponsePacket;
import com.projectwild.shared.packets.world.WorldDataPacket;

import java.nio.ByteBuffer;

public class WorldListener extends Listener {
    
    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof RequestWorldPacket) {
            RequestWorldPacket packet = (RequestWorldPacket) obj;
    
            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
            if(client == null)
                return;

            connection.sendTCP(new RequestWorldResponsePacket(true, null));
            
            World world = WildServer.getWorldHandler().getWorld(packet.getWorld());
            
            Block[][][] blocks = world.getBlocks();
            ByteBuffer buffer = ByteBuffer.allocate(0);
            for(int y = 0; y < blocks.length; y++) {
                for(int x = 0; x < blocks[y].length; x++) {
                    for(int z = 0; z < 2; z++) {
                        Block block = blocks[y][x][z];
                        byte[] data = block.serializeForClient();
                        buffer = ByteBuffer.allocate(buffer.capacity() + 8 + data.length).put(buffer.array()).putInt(block.getBlockPreset().getId()).putInt(data.length).put(data);
                    }
                }
            }
            connection.sendTCP(new WorldDataPacket(blocks[0].length, blocks.length, buffer.array()));

            client.setPlayer(world.createPlayer(client));
        }
    }
    
}
