package com.projectwild.game.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectwild.game.WildGame;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.ItemTypes;

public class InventoryHandler {

    private ItemStack[] inventory;
    private int activeSlot = 0;

    public InventoryHandler() {
        inventory = new ItemStack[48];
    }

    public void updateInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void changeItems(int slot, ItemStack itemStack) {
        inventory[slot] = itemStack;
    }

    public void render(SpriteBatch sb) {
        Texture slotTexture = WildGame.getAssetManager().getAsset("inventory_slot");
        Texture activeSlotTexture = WildGame.getAssetManager().getAsset("inventory_slot_active");
        for(int i = 0; i < 8; i++) {
            if(activeSlot == i) {
                sb.draw(activeSlotTexture, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40, 0);
            } else {
                sb.draw(slotTexture, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40, 0);
            }

            if(inventory[i] != null) {
                ItemPreset itemPreset = inventory[i].getItemPreset();
                if(itemPreset.getItemType() == ItemTypes.BLOCK.getId()) {
                    BlockPreset blockPreset = BlockPreset.getPreset(itemPreset.getBlockId());
                    TextureRegion texture = WildGame.getAssetManager().getTile(blockPreset.getTileset(), blockPreset.getTilesetX(), blockPreset.getTilesetY());;
                    sb.draw(texture, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40 + 10, 10, 20, 20);

                    BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
                    GlyphLayout layout = new GlyphLayout(font, Integer.toString(inventory[i].getAmount()));
                    font.draw(sb, layout, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40 + 35 - layout.width, 15);
                }
            }
        }
    }

    public boolean handleMouseInput(int screenX, int screenY) {
        if(screenY >= Gdx.graphics.getHeight() - 40) {
            for(int i = 0; i < 8; i++) {
                int x = Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40;
                if(screenX >= x && screenX < x + 40) {
                    activeSlot = i;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean handleKeyInput(int keycode) {
        int num = keycode - 8;
        if(num >= 0 && num < 8)
            activeSlot = num;
        return false;
    }

    public int getActiveSlot() {
        return activeSlot;
    }

}
