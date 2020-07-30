package com.projectwild.game.ingame.blocks.types;

import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;

public class HurtingBlock extends Block {

    public HurtingBlock(BlockPreset blockPreset, byte[] data, int x, int y, int z) {
        super(blockPreset, data, x, y, z);
    }

    @Override
    public boolean collide() {
        callNWCallback("collision");
        return true;
    }
}
