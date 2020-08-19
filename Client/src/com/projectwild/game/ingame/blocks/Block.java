package com.projectwild.game.ingame.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.world.CallNetworkedCallbackPacket;
import com.projectwild.shared.utils.Vector2;

import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class Block {

    private BlockPreset blockPreset;

    private int x, y, z;

    private HashMap<String, Callback> networkedCallbacks;
    private HashMap<String, Object> networkedVariables;

    public Block(BlockPreset blockPreset, byte[] data, int x, int y, int z) {
        this.blockPreset = blockPreset;
        this.x = x;
        this.y = y;
        this.z = z;

        // Loading Networked Variables
        networkedVariables = new HashMap<>();
        networkedCallbacks = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int variables = buffer.getInt();
        for(int i = 0; i < variables; i++) {
            StringBuilder key = new StringBuilder();
            int keyLength = buffer.getInt();
            for(int j = 0; j < keyLength; j++) {
                key.append((char) buffer.get());
            }
            int type = buffer.getInt();
            if(type == 0) {
                StringBuilder value = new StringBuilder();
                int valueLength = buffer.getInt();
                for(int j = 0; j < valueLength; j++) {
                    value.append((char) buffer.get());
                }
                networkedVariables.put(key.toString(), value.toString());
            } else if(type == 1) {
                networkedVariables.put(key.toString(), buffer.getInt() == 1);
            } else {
                networkedVariables.put(key.toString(), buffer.getDouble());
            }
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {}
    
    public void renderShadow(SpriteBatch sb, ShapeRenderer sr) {}

    public boolean collide() {
        return true;
    }
    
    public BlockPreset getBlockPreset() {
        return blockPreset;
    }

    public void setNWCallback(String key, Callback callback) {
        networkedCallbacks.put(key, callback);
    }

    public void callNWCallback(String key, Object... data) {
        CallNetworkedCallbackPacket packet = new CallNetworkedCallbackPacket(x, y, z, key, data);
        WildGame.getClient().sendTCP(packet);
    }

    public Callback getNWCallback(String key) {
        if(networkedCallbacks.containsKey(key))
            return networkedCallbacks.get(key);
        return null;
    }

    public void setNWVar(String key, Object value) {
        networkedVariables.put(key, value);
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
        return (boolean) networkedVariables.get(key);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public interface Callback {

        void callback(Object[] data);

    }

}
