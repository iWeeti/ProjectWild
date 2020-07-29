package com.projectwild.game.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.ItemTypes;
import com.projectwild.shared.packets.items.ChangeInventoryItemPacket;
import com.projectwild.shared.packets.player.local.MoveItemSlotsPacket;

public class InventoryHandler {

    private boolean inventoryOpen;
    private int draggingSlot;
    private ItemStack[] inventory;
    private int activeSlot = 0;

    public InventoryHandler() {
        inventory = new ItemStack[48];
        draggingSlot = -1;
    }

    public void updateInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void changeItems(int slot, ItemStack itemStack) {
        inventory[slot] = itemStack;
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        Texture slotTexture = WildGame.getAssetManager().getAsset("inventory_slot");
        Texture activeSlotTexture = WildGame.getAssetManager().getAsset("inventory_slot_active");

        if(inventoryOpen) {
            // Draw Background
            sb.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(0, 0, 0, 0.5f);
                sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            sb.begin();

            // Draw Inventory
            for(int x = 0; x < 6; x++) {
                for(int y = 0; y < 5; y++) {
                    sb.draw(slotTexture, 20 + x * 40, 20 + y * 40);

                    int slot = 8 + (y * 6) + x;

                    if(draggingSlot == slot)
                        continue;

                    if(inventory[slot] != null) {
                        ItemPreset itemPreset = inventory[slot].getItemPreset();
                        if(itemPreset.getItemType() == ItemTypes.BLOCK.getId()) {
                            BlockPreset blockPreset = BlockPreset.getPreset(itemPreset.getBlockId());
                            TextureRegion texture = WildGame.getAssetManager().getTile(blockPreset.getTileset(), blockPreset.getTilesetX(), blockPreset.getTilesetY());
                            sb.draw(texture, 20 + x * 40 + 10, 20 + y * 40 + 10, 20, 20);
                        }

                        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
                        GlyphLayout layout = new GlyphLayout(font, Integer.toString(inventory[slot].getAmount()));
                        font.draw(sb, layout, 20 + x * 40 + 35 - layout.width, 20 + y * 40 + 15);
                    }
                }
            }

        }

        // Draw The Hotbar
        for(int i = 0; i < 8; i++) {
            if(activeSlot == i) {
                sb.draw(activeSlotTexture, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40, 0);
            } else {
                sb.draw(slotTexture, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40, 0);
            }

            if(draggingSlot == i)
                continue;

            if(inventory[i] != null) {
                ItemPreset itemPreset = inventory[i].getItemPreset();
                if(itemPreset.getItemType() == ItemTypes.BLOCK.getId()) {
                    BlockPreset blockPreset = BlockPreset.getPreset(itemPreset.getBlockId());
                    TextureRegion texture = WildGame.getAssetManager().getTile(blockPreset.getTileset(), blockPreset.getTilesetX(), blockPreset.getTilesetY());
                    sb.draw(texture, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40 + 10, 10, 20, 20);
                }

                BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
                GlyphLayout layout = new GlyphLayout(font, Integer.toString(inventory[i].getAmount()));
                font.draw(sb, layout, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40 + 35 - layout.width, 15);
            }
        }

        if(inventoryOpen) {
            // Draw Drag Thing
            if(draggingSlot != -1) {
                ItemPreset itemPreset = inventory[draggingSlot].getItemPreset();
                if (itemPreset.getItemType() == ItemTypes.BLOCK.getId()) {
                    BlockPreset blockPreset = BlockPreset.getPreset(itemPreset.getBlockId());
                    TextureRegion texture = WildGame.getAssetManager().getTile(blockPreset.getTileset(), blockPreset.getTilesetX(), blockPreset.getTilesetY());
                    sb.draw(texture, Gdx.input.getX() + 15, Gdx.graphics.getHeight() - (Gdx.input.getY() + 30), 20, 20);
                }

                BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
                GlyphLayout layout = new GlyphLayout(font, Integer.toString(inventory[draggingSlot].getAmount()));
                font.draw(sb, layout, Gdx.input.getX() + 40 - layout.width, Gdx.graphics.getHeight() - (Gdx.input.getY() + 25));
            }
        }
    }

    public boolean mouseDown(int screenX, int screenY) {
        if(inventoryOpen) {
            // Check For Clicks Inside Inventory
            int x = (int) Math.floor((screenX - 20) / 40f);
            int y = (int) Math.floor((Gdx.graphics.getHeight() - screenY - 20) / 40f);

            if(x >= 0 && x < 6 && y >= 0 && y < 5) {
                int slot = 8 + y * 6 + x;
                if(inventory[slot] != null)
                    draggingSlot = slot;
            }
        }

        // Check For Clicks Inside Hotbar
        if(screenY >= Gdx.graphics.getHeight() - 40) {
            for(int i = 0; i < 8; i++) {
                int x = Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40;
                if(screenX >= x && screenX < x + 40) {
                    if(!inventoryOpen) {
                        activeSlot = i;
                    } else {
                        if(inventory[i] != null)
                            draggingSlot = i;
                    }
                    return true;
                }
            }
        }

        return inventoryOpen;
    }

    public boolean mouseUp(int screenX, int screenY) {
        if(!inventoryOpen)
            return false;

        if(draggingSlot == -1)
            return false;

        // Setting Variables And Resetting Drag
        int draggedSlot = draggingSlot;
        int droppedSlot = -1;

        draggingSlot = -1;

        // Check For Clicks Inside Inventory
        int x = (int) Math.floor((screenX - 20) / 40f);
        int y = (int) Math.floor((Gdx.graphics.getHeight() - screenY - 20) / 40f);

        if(x >= 0 && x < 6 && y >= 0 && y < 5)
            droppedSlot = 8 + y * 6 + x;

        // Check For Clicks Inside Hotbar
        if(screenY >= Gdx.graphics.getHeight() - 40) {
            for(int i = 0; i < 8; i++) {
                x = Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40;
                if(screenX >= x && screenX < x + 40) {
                    droppedSlot = i;
                    break;
                }
            }
        }

        // Slot Not Detected
        if(droppedSlot < 0)
            return false;

        ItemStack temp = inventory[droppedSlot];
        inventory[droppedSlot] = inventory[draggedSlot];
        inventory[draggedSlot] = temp;

        MoveItemSlotsPacket packet = new MoveItemSlotsPacket(draggedSlot, droppedSlot);
        WildGame.getClient().sendTCP(packet);

        return true;
    }

    public boolean handleKeyInput(int keycode) {
        if(keycode == Input.Keys.TAB) {
            inventoryOpen = !inventoryOpen;
            return false;
        }

        int num = keycode - 8;
        if(num >= 0 && num < 8)
            activeSlot = num;
        return false;
    }

    public int getActiveSlot() {
        return activeSlot;
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }

}
