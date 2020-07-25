package com.projectwild.server.worlds;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.blocks.BlockTypes;
import com.projectwild.server.worlds.blocks.types.StaticBlock;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.player.PlayerRemovePacket;
import com.projectwild.shared.packets.player.PlayerSpawnPacket;
import com.projectwild.shared.utils.Vector2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {

    private String name;
    private Block[][][] blocks;
    private Vector2 spawnPosition;
    
    private int owner;
    private ArrayList<Integer> trusted;

    private CopyOnWriteArrayList<Player> players;
    
    public World(String name) {
        this.name = name.toLowerCase();
        this.owner = -1;
        this.trusted = new ArrayList<>();
        this.players = new CopyOnWriteArrayList<>();
        try {
            // World Loading
            ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(Paths.get(String.format("data/worlds/%s.world", this.name))));
        
            owner = buffer.getInt();
            for(int i = 0; i < buffer.getInt(); i++) {
                trusted.add(buffer.getInt());
            }

            spawnPosition = new Vector2(buffer.getInt(), buffer.getInt());
        
            blocks = new Block[buffer.getInt()][buffer.getInt()][2];
            for(int y = 0; y < blocks.length; y++) {
                for(int x = 0; x < blocks[y].length; x++) {
                    for(int z = 0; z < 2; z++) {
                        int id = buffer.getInt();
                        byte[] extra = new byte[buffer.getInt()];
                        for(int i = 0; i < extra.length; i++) {
                            extra[i] = buffer.get();
                        }
                        try {
                            BlockPreset preset = BlockPreset.getPreset(id);
                            Block block = BlockTypes.getBlockClass(preset.getBlockType()).getConstructor(BlockPreset.class).newInstance(preset);
                            block.deserialize(extra);
                            blocks[y][x][z] = block;
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            // World Generation
            spawnPosition = new Vector2(100*32/2, 31*32);

            blocks = new Block[60][100][2];
            for(int y = 0; y < blocks.length; y++) {
                for(int x = 0; x < blocks[y].length; x++) {
                    for(int z = 0; z < 2; z++) {
                        if(y <= 30 && z == 1) {
                            blocks[y][x][z] = new StaticBlock(BlockPreset.getPreset(1));
                        } else {
                            blocks[y][x][z] = new StaticBlock(BlockPreset.getPreset(0));
                        }
                    }
                }
            }
        }
    }
    
    public void save() {
        ByteBuffer buffer = ByteBuffer.allocate(trusted.size() * 4 + 16);
        buffer.putInt(owner);
        buffer.putInt(trusted.size());
        for(Integer integer : trusted) {
            buffer.putInt(integer);
        }
        buffer.putInt(spawnPosition.getXInt());
        buffer.putInt(spawnPosition.getYInt());
        buffer.putInt(blocks.length);
        buffer.putInt(blocks[0].length);
        for(int y = 0; y < blocks.length; y++) {
            for(int x = 0; x < blocks[y].length; x++) {
                for(int z = 0; z < 2; z++) {
                    Block block = blocks[y][x][z];
                    byte[] data = block.serialize();
                    buffer = ByteBuffer.allocate(buffer.capacity() + 8 + data.length).put(buffer.array()).putInt(block.getBlockPreset().getId()).putInt(data.length).put(data);
                }
            }
        }
        try {
            Files.write(Paths.get(String.format("data/worlds/%s.world", name)), buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player createPlayer(Client client) {
        Player player = new Player(client, this, spawnPosition);

        // Sending Local Player To Client
        PlayerSpawnPacket playerSpawnPacket = new PlayerSpawnPacket(client.getUserId(), client.getUsername(), spawnPosition, true);
        WildServer.getServer().sendToTCP(client.getSocket(), playerSpawnPacket);

        // Sending All Players To Client
        for(Player ply : players) {
            playerSpawnPacket = new PlayerSpawnPacket(ply.getClient().getUserId(), ply.getClient().getUsername(), ply.getPosition(), false);
            WildServer.getServer().sendToTCP(client.getSocket(), playerSpawnPacket);
        }

        // Broadcasting New Player With All Clients In The World
        playerSpawnPacket = new PlayerSpawnPacket(client.getUserId(), client.getUsername(), spawnPosition, false);
        for(Player ply : players)
            WildServer.getServer().sendToTCP(ply.getClient().getSocket(), playerSpawnPacket);

        players.add(player);

        return player;
    }

    public void destroyPlayer(Player player) {
        players.remove(player);
        PlayerRemovePacket playerRemovePacket = new PlayerRemovePacket(player.getClient().getUserId());
        for(Player ply : players) {
            WildServer.getServer().sendToTCP(ply.getClient().getSocket(), playerRemovePacket);
        }
    }

    public int pointCollisionType(Vector2 point) {
        int blockX = (int) Math.floor((point.getX()) / 32.0f);
        int blockY = (int) Math.floor((point.getY()) / 32.0f);

        if(blockX >= blocks[0].length || blockX < 0 || blockY >= blocks.length || blockY < 0)
            return 1;

        return blocks[blockY][blockX][1].getBlockPreset().getCollisionType();
    }
    
    public String getName() {
        return name;
    }
    
    public Block[][][] getBlocks() {
        return blocks;
    }
    
    public int getOwner() {
        return owner;
    }
    
    public ArrayList<Integer> getTrusted() {
        return trusted;
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

}
