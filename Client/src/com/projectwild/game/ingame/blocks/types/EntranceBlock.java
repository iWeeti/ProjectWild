package com.projectwild.game.ingame.blocks.types;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

public class EntranceBlock extends Block {

    public EntranceBlock(BlockPreset blockPreset, byte[] data, int x, int y, int z) {
        super(blockPreset, data, x, y, z);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 position) {
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
    public boolean collide() {
        LocalPlayer localPlayer = ((WorldState) WildGame.getState()).getWorld().getLocalPlayer();
        if(localPlayer.hasAccess())
            return false;
        return true;
    }

}
