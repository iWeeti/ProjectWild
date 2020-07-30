package com.projectwild.game.ingame.blocks.types;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

public class StaticBlock extends Block {

    public StaticBlock(BlockPreset blockPreset, byte[] data, int x, int y, int z) {
        super(blockPreset, data, x, y, z);
    }

}
