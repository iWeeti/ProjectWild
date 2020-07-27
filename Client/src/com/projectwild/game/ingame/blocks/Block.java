package com.projectwild.game.ingame.blocks;

import com.projectwild.shared.BlockPreset;

public abstract class Block {

    private BlockPreset blockPreset;
    
    public Block(BlockPreset blockPreset) {
        this.blockPreset = blockPreset;
    }
    
    public abstract void deserialize(byte[] data);
    
    public BlockPreset getBlockPreset() {
        return blockPreset;
    }

}
