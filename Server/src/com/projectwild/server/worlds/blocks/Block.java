package com.projectwild.server.worlds.blocks;

import com.projectwild.server.WildServer;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.world.UpdateNetworkedVariablePacket;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public abstract class Block {
    
    private BlockPreset blockPreset;

    private World world;
    private int x, y, z;

    private HashMap<String, Object> networkedVariables;

    public Block(BlockPreset preset, World world, int x, int y, int z) {
        networkedVariables = new HashMap<>();
        this.blockPreset = preset;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public abstract void deserialize(byte[] data);
    
    public abstract byte[] serialize();

    public abstract void collision(Player ply);

    public abstract void update();

    public BlockPreset getBlockPreset() {
        return blockPreset;
    }

    public HashMap<String, Object> getNetworkedVariables() {
        return networkedVariables;
    }

    public byte[] serializeNetworkVariables() {
        int length = 4;
        for(String s : networkedVariables.keySet()) {
            length += s.getBytes().length + 4;
        }
        for(Object o : networkedVariables.values()) {
            length += 4;
            if(o instanceof String) {
                length += ((String) o).getBytes().length + 4;
            } else if(o instanceof Boolean) {
                length += 4;
            } else {
                length += 8;
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.putInt(networkedVariables.size());
        for(Map.Entry entry : networkedVariables.entrySet()) {
            byte[] key = ((String) entry.getKey()).getBytes();
            buffer.putInt(key.length);
            buffer.put(key);

            if(entry.getValue() instanceof String) {
                buffer.putInt(0);
                byte[] value = ((String) entry.getValue()).getBytes();
                buffer.putInt(value.length);
                buffer.put(value);
            } else if(entry.getValue() instanceof Boolean) {
                buffer.putInt(1);
                buffer.putInt(((Boolean) entry.getValue()) ? 1 : 0);
            } else {
                buffer.putInt(2);
                buffer.putDouble((double) entry.getValue());
            }
        }
        return buffer.array();
    }

    private void setNWVar(String key, Object value) {
        networkedVariables.put(key, value);
        UpdateNetworkedVariablePacket variablePacket = new UpdateNetworkedVariablePacket(x, y, z, key, value);
        for(Player ply : world.getPlayers())
            WildServer.getServer().sendToTCP(ply.getClient().getSocket(), variablePacket);
    }

    public void setNWString(String key, String value) {
        setNWVar(key, value);
    }

    public void setNWInt(String key, int value) {
        setNWVar(key, (double) value);
    }

    public void setNWDouble(String key, double value) {
        setNWVar(key, value);
    }

    public void setNWBool(String key, Boolean value) {
        setNWVar(key, value);
    }

    public String getNWString(String key) {
        return (String) networkedVariables.get(key);
    }

    public int getNWInt(String key) {
        return (int) (double) networkedVariables.get(key);
    }

    public double getNWDouble(String key) {
        return (double) networkedVariables.get(key);
    }

    public boolean getNWBool(String key) {
        return (Boolean) networkedVariables.get(key);
    }

}
