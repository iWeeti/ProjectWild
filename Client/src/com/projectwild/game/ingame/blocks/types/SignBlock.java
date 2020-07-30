package com.projectwild.game.ingame.blocks.types;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

public class SignBlock extends Block {

    public SignBlock(BlockPreset blockPreset, byte[] data) {
        super(blockPreset, data);
    }

    @Override
    public void render(SpriteBatch sb, Vector2 position) {
        TextureRegion tex = WildGame.getAssetManager().getTile(getBlockPreset().getTileset(), getBlockPreset().getTilesetX(), getBlockPreset().getTilesetY());
        sb.draw(tex, (int) position.getX() * 32, (int) position.getY() * 32);

        String text = getNWString("text");
        if (text == null)
            return;
        System.out.println(text);
        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(sb, layout, (int) position.getX() * 32 - layout.width / 2 + 16 , (int) position.getY() * 32 - layout.height / 2 - 16);
    }
}
