package com.projectwild.game.ingame.gui.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.gui.GUIComponent;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.ItemTypes;

public class GUIItemFrameComponent extends GUIComponent {

    private ItemStack itemStack;
    private float sizeMultiplier;

    public GUIItemFrameComponent(ItemStack itemStack) {
        this(itemStack, 1);
    }

    public GUIItemFrameComponent(ItemStack itemStack, float sizeMultiplier) {
        this.itemStack = itemStack;
        this.sizeMultiplier = sizeMultiplier;
        setWidth((int) (40 * sizeMultiplier));
        setHeight((int) (40 * sizeMultiplier));
    }

    @Override
    protected void render(SpriteBatch sb, ShapeRenderer sr) {
        Texture slot = WildGame.getAssetManager().getAsset("inventory_slot");
        sb.draw(slot, getX(), getY(), getWidth(), getHeight());

        ItemPreset itemPreset = itemStack.getItemPreset();
        TextureRegion itemTexture = null;
        switch(ItemTypes.getType(itemPreset.getItemType())) {
            case ITEM:
            case CLOTHING:
                itemTexture = WildGame.getAssetManager().getItemIcon(itemPreset.getItemSet(), itemPreset.getItemSetX(), itemPreset.getItemSetY());
                break;
            case BLOCK:
                BlockPreset blockPreset = BlockPreset.getPreset(itemPreset.getBlockId());
                itemTexture = WildGame.getAssetManager().getTile(blockPreset.getTileset(), blockPreset.getTilesetX(), blockPreset.getTilesetY());
                break;
        }

        sb.draw(itemTexture, getX() + getWidth() / 4f, getY() + getWidth() / 4f, getWidth() / 2f, getHeight() / 2f);
    }

    @Override
    protected void mouseDown(int x, int y) {

    }

    @Override
    protected void mouseUp(int x, int y) {

    }

    @Override
    protected void typed(char character) {

    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
