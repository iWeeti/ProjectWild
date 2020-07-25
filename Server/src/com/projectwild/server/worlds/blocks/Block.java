package com.projectwild.server.worlds.blocks;

import com.projectwild.shared.BlockPreset;

public abstract class Block {
    
    private BlockPreset blockPreset;
    
    public Block(BlockPreset preset) {
        this.blockPreset = preset;
    }

    public abstract void deserialize(byte[] data);
    
    public abstract byte[] serialize();
    
    public abstract byte[] serializeForClient();
    
    public BlockPreset getBlockPreset() {
        return blockPreset;
    }
    
}
