package com.projectwild.game.ingame.blocks.types;

import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.shared.BlockPreset;

public class StaticBlock extends Block {
    
    public StaticBlock(BlockPreset blockPreset) {
        super(blockPreset);
    }
    
    @Override
    public void deserialize(byte[] data) {}
    
}
