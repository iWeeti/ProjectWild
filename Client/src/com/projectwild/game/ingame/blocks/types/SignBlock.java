package com.projectwild.game.ingame.blocks.types;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.utils.Vector2;

public class SignBlock extends Block {

    private BitmapFont font;
    private boolean onSign;

    public SignBlock(BlockPreset blockPreset, byte[] data, int x, int y, int z) {
        super(blockPreset, data, x, y, z);
        font = WildGame.getAssetManager().getFont("vcr_osd_12");
        onSign = false;
    }

    @Override
    public void render(SpriteBatch sb, Vector2 position) {
        TextureRegion tex = WildGame.getAssetManager().getTile(getBlockPreset().getTileset(), getBlockPreset().getTilesetX(), getBlockPreset().getTilesetY());
        sb.draw(tex, (int) position.getX() * 32, (int) position.getY() * 32);
        if (onSign){
            String text = getNWString("text");
            if (text == null || text.isEmpty()){
                text = "/settext <text>";
            }
            GlyphLayout layout = new GlyphLayout(font, text);
            font.draw(sb, layout, (int) position.getX() * 32 - layout.width / 2 + 16 , (int) position.getY() * 32 + 38);
        }
        onSign = false;
    }

    @Override
    public boolean collide() {
        onSign = true;
        return true;
    }
}
