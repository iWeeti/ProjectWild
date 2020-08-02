package com.projectwild.game.ingame.blocks.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        font = WildGame.getAssetManager().getFont("vcr_osd_32");
        font.getData().setScale(0.35f);
        onSign = false;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 position) {
        TextureRegion tex = WildGame.getAssetManager().getTile(getBlockPreset().getTileset(), getBlockPreset().getTilesetX(), getBlockPreset().getTilesetY());
        sb.draw(tex, (int) position.getX() * 32, (int) position.getY() * 32);
        if (onSign){
            String text = getNWString("text");
            if (text == null || text.isEmpty()){
                text = "/settext <text>";
            }
            GlyphLayout layout = new GlyphLayout(font, text);
            sb.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0, 0, 0, 0.5f);
            sr.rect((int) position.getX() * 32 - layout.width / 2 + 11 , (int) position.getY() * 32 + 38 + 10, layout.width + 10, layout.height/2 + 10);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            sb.begin();
            font.draw(sb, layout, (int) position.getX() * 32 - layout.width / 2 + 16 , (int) position.getY() * 32 + 38 + 20);
        }
        onSign = false;
    }

    @Override
    public boolean collide() {
        onSign = true;
        return true;
    }
}
