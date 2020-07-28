package com.projectwild.game.ingame.blocks.types;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

public class EntranceBlock extends Block {

    public EntranceBlock(BlockPreset blockPreset, byte[] data) {
        super(blockPreset, data);
    }

    @Override
    public void render(SpriteBatch sb, Vector2 position) {
        BlockPreset preset = getBlockPreset();
        TextureRegion texture;
        if(getNWBool("open")) {
            texture = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 1, preset.getTilesetY());
        } else {
            texture = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX(), preset.getTilesetY());
        }
        sb.draw(texture, (int) position.getX() * 32, (int) position.getY() * 32);
    }

    @Override
    public double collide(LocalPlayer player, double startingVelocity) {
        if(player.hasAccess()) {
            return startingVelocity;
        } else {
            return 0;
        }
    }

}
