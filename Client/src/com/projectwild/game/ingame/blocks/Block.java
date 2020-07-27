package com.projectwild.game.ingame.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

public abstract class Block {

    private BlockPreset blockPreset;
    
    public Block(BlockPreset blockPreset) {
        this.blockPreset = blockPreset;
    }
    
    public void deserialize(byte[] data) {}

    public void render(SpriteBatch sb, Vector2 position) {}

    public double collide(LocalPlayer player, double startingVelocity) {
        return startingVelocity;
    }
    
    public BlockPreset getBlockPreset() {
        return blockPreset;
    }

}
