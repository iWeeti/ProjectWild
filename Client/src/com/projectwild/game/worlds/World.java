package com.projectwild.game.worlds;

import com.projectwild.game.worlds.blocks.Block;
import com.projectwild.game.worlds.blocks.BlockTypes;
import com.projectwild.game.worlds.player.LocalPlayer;
import com.projectwild.game.worlds.player.Player;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.world.WorldDataPacket;
import com.projectwild.shared.utils.Vector2;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {

    public CopyOnWriteArrayList<Player> players;

    public LocalPlayer localPlayer;

    private Block[][][] blocks;
    private int width, height;

    public World(WorldDataPacket dataPacket) {
        players = new CopyOnWriteArrayList<>();

        width = dataPacket.getWidth();
        height = dataPacket.getHeight();

        blocks = new Block[height][width][2];
        ByteBuffer buffer = ByteBuffer.wrap(dataPacket.getBlockData());
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
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
    }

    public int pointCollisionType(Vector2 point) {
        int blockX = (int) Math.floor((point.getX()) / 32.0f);
        int blockY = (int) Math.floor((point.getY()) / 32.0f);

        if(blockX >= getWidth() || blockX < 0 || blockY >= getHeight() || blockY < 0)
            return 1;

        return blocks[blockY][blockX][1].getBlockPreset().getCollisionType();
    }

    public Player createPlayer(int userId, String username) {
        Player player = new Player(userId, username);
        players.add(player);
        return player;
    }

    public void removePlayer(int userId) {
        for(Player ply : players) {
            if(ply.getUserId() == userId)
                players.remove(ply);
        }
    }

    public LocalPlayer createLocalPlayer(int userId, String username) {
        localPlayer = new LocalPlayer(userId, username);
        return localPlayer;
    }

    public Player getPlayer(int userId) {
        for(Player ply : players) {
            if(ply.getUserId() == userId)
                return ply;
        }
        return null;
    }

    public LocalPlayer getLocalPlayer() {
        return localPlayer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

}
