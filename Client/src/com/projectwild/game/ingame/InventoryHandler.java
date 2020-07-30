package com.projectwild.game.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.shared.*;
import com.projectwild.shared.packets.clothing.EquipPacket;
import com.projectwild.shared.packets.items.MoveItemSlotsPacket;

public class InventoryHandler {

    private boolean inventoryOpen;
    private boolean draggingEquipped;
    private int draggingSlot;
    private ItemStack[] inventory;
    private int activeSlot = 0;

    public InventoryHandler() {
        inventory = new ItemStack[44];
        draggingSlot = -1;
    }

    public void updateInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void changeItems(int slot, ItemStack itemStack) {
        inventory[slot] = itemStack;
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
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
                for(int y = 0; y < inventory.length / 6; y++) {
                    int slot = 8 + (y * 6) + x;
                    if(inventory.length <= slot)
                        break;
                    drawSlot(sb, slot, 20 + x * 40, 20 + y * 40);
                }
            }

            // Draw Equipped slots
            for(int i = 0; i < 9; i++) {
                sb.draw(WildGame.getAssetManager().getAsset("inventory_slot"), 280, 20 + i * 40);
                if(getEquipped()[i] != null)
                    drawItemStack(sb, getEquipped()[i], 290, 30 + i * 40);
            }
        }

        // Draw The Hotbar
        for(int i = 0; i < 8; i++)
            drawSlot(sb, i, Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40, 0);

        // Draw Drag Thing
        if(inventoryOpen)
            if(draggingSlot != -1)
                drawItemStack(sb, draggingEquipped ? getEquipped()[draggingSlot] : inventory[draggingSlot], Gdx.input.getX() + 15, Gdx.graphics.getHeight() - (Gdx.input.getY() + 30));
    }

    private ItemStack[] getEquipped() {
        return ((WorldState) WildGame.getState()).getWorld().getLocalPlayer().getEquipped();
    }

    private void drawSlot(SpriteBatch sb, int slot, int x, int y) {
        if(activeSlot == slot) {
            sb.draw(WildGame.getAssetManager().getAsset("inventory_slot_active"), x, y);
        } else {
            sb.draw(WildGame.getAssetManager().getAsset("inventory_slot"), x, y);
        }

        if(draggingSlot == slot && !draggingEquipped)
            return;

        if(inventory[slot] == null)
            return;

        drawItemStack(sb, inventory[slot], x + 10, y + 10);
    }

    private void drawItemStack(SpriteBatch sb, ItemStack stack, int x, int y) {
        ItemPreset itemPreset = stack.getItemPreset();
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

        if(itemTexture != null)
            sb.draw(itemTexture, x, y, 20, 20);

        if(stack.getAmount() == 1)
            return;

        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
        GlyphLayout layout = new GlyphLayout(font, Integer.toString(stack.getAmount()));
        font.draw(sb, layout, x + 25 - layout.width, y + 5);
    }

    public boolean mouseDown(int screenX, int screenY) {
        if(inventoryOpen) {
            // Check For Clicks Inside Inventory
            {
                int x = (int) Math.floor((screenX - 20) / 40f);
                int y = (int) Math.floor((Gdx.graphics.getHeight() - screenY - 20) / 40f);

                if (x >= 0 && x < 6 && y >= 0 && y < inventory.length / 6) {
                    int slot = 8 + y * 6 + x;
                    if (inventory[slot] != null)
                        draggingSlot = slot;
                }
            }

            // Check For Clicks Inside Equipped
            {
                int x = (int) Math.floor((screenX - 280) / 40f);
                int y = (int) Math.floor((Gdx.graphics.getHeight() - screenY - 20) / 40f);

                if(x == 0 && y >= 0 && y < 9) {
                    if(getEquipped()[y] != null) {
                        draggingEquipped = true;
                        draggingSlot = y;
                    }
                }
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
        boolean draggedEquipped = draggingEquipped;
        int draggedSlot = draggingSlot;
        int droppedSlot = -1;

        draggingEquipped = false;
        draggingSlot = -1;

        // Check For Clicks Inside Inventory
        {
            int x = (int) Math.floor((screenX - 20) / 40f);
            int y = (int) Math.floor((Gdx.graphics.getHeight() - screenY - 20) / 40f);

            if (x >= 0 && x < 6 && y >= 0 && y < inventory.length / 6)
                droppedSlot = 8 + y * 6 + x;
        }

        // Check For Clicks Inside Equipped
        {
            int x = (int) Math.floor((screenX - 280) / 40f);
            int y = (int) Math.floor((Gdx.graphics.getHeight() - screenY - 20) / 40f);

            if(x == 0 && y >= 0 && y < 9) {
                if(getEquipped()[y] == null && !draggedEquipped) {
                    WildGame.getClient().sendTCP(new EquipPacket(draggedSlot, y));
                    return true;
                }
            }
        }

        // Check For Clicks Inside Hotbar
        if(screenY >= Gdx.graphics.getHeight() - 40) {
            for(int i = 0; i < 8; i++) {
                int x = Gdx.graphics.getWidth() / 2 - 8 / 2 * 40 + i * 40;
                if(screenX >= x && screenX < x + 40) {
                    droppedSlot = i;
                    break;
                }
            }
        }

        // Slot Not Detected
        if(droppedSlot < 0)
            return false;

        if(draggedEquipped) {
            WildGame.getClient().sendTCP(new EquipPacket(droppedSlot, draggedSlot));
            return true;
        }

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
