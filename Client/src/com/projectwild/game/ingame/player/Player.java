package com.projectwild.game.ingame.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectwild.game.WildGame;
import com.projectwild.shared.ClothingPreset;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.utils.Vector2;

public class Player {

    private int userId;
    private String nametag;
    private Vector2 position;

    private ItemStack[] equipped;

    private PlayerAnimations animation;
    protected float frame;

    public Player(int userId, String nametag) {
        this.userId = userId;
        this.nametag = nametag;
        position = new Vector2();
        equipped = new ItemStack[9];
        changeAnimation(PlayerAnimations.STAND_RIGHT.getId());
    }

    public void changeAnimation(int animationId) {
        animation = PlayerAnimations.getAnimation(animationId);
        frame = 0;
    }

    public void render(SpriteBatch sb) {
        TextureRegion[][] texture = TextureRegion.split(WildGame.getAssetManager().getAsset("player"), 32, 32);
        sb.draw(texture[animation.getId()][(int) Math.floor(frame)], position.getXInt(), position.getYInt());

        if(animation.getSpeed() != 0)
            frame += animation.getSpeed() / 60.0f;

        if(frame >= animation.getLength())
            frame = 0;

        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_10");
        font.getData().markupEnabled = true;
        GlyphLayout layout = new GlyphLayout(font, nametag);
        font.draw(sb, layout, (int) position.getX() + 17 - layout.width / 2, (int) position.getY() + 40);

        for(ItemStack itemStack : equipped) {
            if(itemStack == null)
                continue;

            ClothingPreset clothingPreset = ClothingPreset.getPreset(itemStack.getItemPreset().getClothingId());

            TextureRegion[][] clothingTexture = WildGame.getAssetManager().getClothingAsset(clothingPreset.getAsset());
            sb.draw(clothingTexture[animation.getId()][(int) Math.floor(frame)], position.getXInt(), position.getYInt());
        }
    }

    public void setEquipped(ItemStack[] equipped) {
        this.equipped = equipped;
    }

    public void setNametag(String nametag) {
        this.nametag = nametag;
    }

    public int getUserId() {
        return userId;
    }

    public String getNametag() {
        return nametag;
    }

    public ItemStack[] getEquipped() {
        return equipped;
    }

    public Vector2 getPosition() {
        return position;
    }

    public PlayerAnimations getAnimation() {
        return animation;
    }

}
