package com.projectwild.game.ingame.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class Block {

    private BlockPreset blockPreset;

    private HashMap<String, Object> networkedVariables;
    
    public Block(BlockPreset blockPreset, byte[] data) {
        this.blockPreset = blockPreset;

        // Loading Networked Variables
        networkedVariables = new HashMap<>();
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

    public void render(SpriteBatch sb, Vector2 position) {}

    public double collide(LocalPlayer player, double startingVelocity) {
        return startingVelocity;
    }
    
    public BlockPreset getBlockPreset() {
        return blockPreset;
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

}
