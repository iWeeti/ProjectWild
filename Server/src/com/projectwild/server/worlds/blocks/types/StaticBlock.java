package com.projectwild.server.worlds.blocks.types;

import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.shared.BlockPreset;

public class StaticBlock extends Block {
    
    public StaticBlock(BlockPreset preset) {
        super(preset);
    }
    
    @Override
    public void deserialize(byte[] data) {}
    
    @Override
    public byte[] serialize() {
        return new byte[0];
    }
    
    @Override
    public byte[] serializeForClient() {
        return new byte[0];
    }
    
}
