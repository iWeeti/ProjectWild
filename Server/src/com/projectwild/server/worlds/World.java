package com.projectwild.server.worlds;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.blocks.BlockTypes;
import com.projectwild.server.worlds.blocks.types.StaticBlock;
import com.projectwild.server.worlds.blocks.types.UnbreakableBlock;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.PlaySoundPacket;
import com.projectwild.shared.packets.clothing.UpdateEquippedPacket;
import com.projectwild.shared.packets.player.PlayerRemovePacket;
import com.projectwild.shared.packets.player.PlayerSpawnPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;
import com.projectwild.shared.packets.world.UpdateBlockPacket;
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

    private String background;
    
    private int owner;
    private ArrayList<Integer> trusted;

    private CopyOnWriteArrayList<Player> players;
    
    public World(String name) {
        this.name = name.toLowerCase();
        this.owner = -1;
        this.trusted = new ArrayList<>();
        this.players = new CopyOnWriteArrayList<>();
        this.background = Math.random() > 0.5 ? "forest_bg" : "desert_bg";
        try {
            // World Loading
            ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(Paths.get(String.format("data/worlds/%s.world", this.name))));
        
            owner = buffer.getInt();
            int trustedLength = buffer.getInt();
            for(int i = 0; i < trustedLength; i++) {
                trusted.add(buffer.getInt());
            }

            int backgroundLength = buffer.getInt();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < backgroundLength; i++) {
                builder.append((char) buffer.get());
            }
            background = builder.toString();

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
                            Block block = BlockTypes.getBlockClass(preset.getBlockType()).getConstructor(BlockPreset.class, World.class, int.class, int.class, int.class).newInstance(preset, this, x, y, z);
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
                        if(y <= 1 && z == 1) {
                            blocks[y][x][z] = new UnbreakableBlock(BlockPreset.getPreset(23), this, x, y, z);
                        } else if(y <= 30 && z == 1) {
                            blocks[y][x][z] = new StaticBlock(BlockPreset.getPreset(1), this, x, y, z);
                        } else {
                            blocks[y][x][z] = new StaticBlock(BlockPreset.getPreset(0), this, x, y, z);
                        }
                    }
                }
            }

            blocks[31][50][1] = new UnbreakableBlock(BlockPreset.getPreset(24), this, 50, 31, 1);
            blocks[30][50][1] = new UnbreakableBlock(BlockPreset.getPreset(23), this, 50, 30, 1);

        }
    }
    
    public void save() {
        ByteBuffer buffer = ByteBuffer.allocate(trusted.size() * 4 + 28 + background.getBytes().length);
        buffer.putInt(owner);
        buffer.putInt(trusted.size());
        for(Integer integer : trusted) {
            buffer.putInt(integer);
        }
        buffer.putInt(background.getBytes().length);
        buffer.put(background.getBytes());
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
        PlayerSpawnPacket playerSpawnPacket = new PlayerSpawnPacket(client.getUserId(), player.getNametag(), spawnPosition, true);
        client.sendTCP(playerSpawnPacket);
        UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(hasAccess(client));
        client.sendTCP(hasAccessPacket);

        // Sending All Players To Client
        for (Player ply : players) {
            playerSpawnPacket = new PlayerSpawnPacket(ply.getClient().getUserId(), ply.getNametag(), ply.getPosition(), false);
            if (!player.getClient().isInvisible())
                client.sendTCP(playerSpawnPacket);
        }

        if (!client.isInvisible()) {
            // Broadcasting New Player With All Clients In The World
            playerSpawnPacket = new PlayerSpawnPacket(client.getUserId(), player.getNametag(), spawnPosition, false);
            for (Player ply : players) {
                ply.getClient().sendChatMessage("%s [WHITE]has joined the world.", player.getNametag());
                ply.getClient().sendTCP(playerSpawnPacket);
            }

            UpdateEquippedPacket equippedPacket = new UpdateEquippedPacket(client.getUserId(), client.getEquipped());
            for(Player ply : players)
                ply.getClient().sendTCP(equippedPacket);
        }

        players.add(player);
        client.sendTCP(new UpdateEquippedPacket(client.getUserId(), client.getEquipped()));
        client.sendChatMessage("Welcome to [YELLOW]%s[WHITE]", getName().toUpperCase());

        return player;
    }

    public void destroyPlayer(Player player) {
        if (!player.getClient().isInvisible()){
            PlayerRemovePacket playerRemovePacket = new PlayerRemovePacket(player.getClient().getUserId());
            for(Player ply : players) {
                ply.getClient().sendChatMessage("[YELLOW]%s [WHITE]has left the world.", player.getNametag());
                ply.getClient().sendTCP(playerRemovePacket);
            }
        }
        players.remove(player);

        if(players.size() <= 0)
            WildServer.getWorldHandler().unloadWorld(this);
        else
            playSoundForAll("close_door");
    }

    public void playSoundForAll(String sound) {
        PlaySoundPacket packet = new PlaySoundPacket(sound);
        for (Player p : players){
            p.getClient().sendTCP(packet);
        }
    }

    public int pointCollisionType(Vector2 point) {
        int blockX = (int) Math.floor((point.getX()) / 32.0f);
        int blockY = (int) Math.floor((point.getY()) / 32.0f);

        if(blockX >= blocks[0].length || blockX < 0 || blockY >= blocks.length || blockY < 0)
            return 1;

        return blocks[blockY][blockX][1].getBlockPreset().getCollisionType();
    }

    public Block pointCollisionBlock(Vector2 point) {
        int blockX = (int) Math.floor((point.getX()) / 32.0f) ;
        int blockY = (int) Math.floor((point.getY()) / 32.0f);

        if(blockX >= blocks[0].length || blockX < 0 || blockY >= blocks.length || blockY < 0)
            return null;

        return blocks[blockY][blockX][1];
    }

    public void setBlock(int x, int y, int z, int blockId) {
        BlockPreset blockPreset = BlockPreset.getPreset(blockId);
        try {
            Block block = BlockTypes.getBlockClass(blockPreset.getBlockType()).getConstructor(BlockPreset.class, World.class, int.class, int.class, int.class).newInstance(blockPreset, this, x, y, z);
            blocks[y][x][z] = block;

            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket(x, y, z, block.getBlockPreset().getId(), block.serializeNetworkVariables());
            for(Player ply : players) {
                ply.getClient().sendTCP(updateBlockPacket);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setSpawnPosition(Vector2 position) {
        spawnPosition = position.copy();
    }

    public boolean claimWorld(Client client) {
        if(client == null) {
            owner = -1;
            trusted = new ArrayList<>();
            return true;
        } else {
            if(owner == -1) {
                owner = client.getUserId();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean addTrusted(Client client) {
        if(trusted.size() >= 10 || owner == -1)
            return false;

        trusted.add(client.getUserId());
        return true;
    }

    public boolean removeTrusted(Client client) {
        return trusted.remove((Object) client.getUserId());
    }

    public boolean hasAccess(Client client) {
        if(owner == -1)
            return true;

        if(client.getUserId() == owner)
            return true;

        for(int i : trusted) {
            if(client.getUserId() == i)
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }
    
    public Block[][][] getBlocks() {
        return blocks;
    }

    public int getHeight() {
        return blocks.length;
    }

    public int getWidth() {
        return blocks[0].length;
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

    public String getBackground() {
        return background;
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

}
