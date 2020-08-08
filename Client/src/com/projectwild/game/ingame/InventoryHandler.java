package com.projectwild.game.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
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
import com.projectwild.shared.packets.clothing.EquipPacket;
import com.projectwild.shared.packets.items.MoveItemSlotsPacket;

public class InventoryHandler {
    
    private InventoryInputAdapter inputAdapter;
    private Slot draggingSlot;

    private boolean inventoryOpen;
    private ItemStack[] inventory;
    private int activeSlot = 0;
    
    public InventoryHandler() {
        inventory = new ItemStack[44];
        inputAdapter = new InventoryInputAdapter();
    }

    public void updateInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void changeItems(int slot, ItemStack itemStack) {
        inventory[slot] = itemStack;
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        int slotSize = (int) (40 * WildGame.getGUIScale());
        int itemSize = (int) (20 * WildGame.getGUIScale());
    
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
                    drawSlot(sb, slot, 20 + x * slotSize, 20 + y * slotSize);
                }
            }

            // Draw Equipped slots
            for(int i = 0; i < 9; i++) {
                if(getEquipped()[i] == null && i > 0) {
                    sb.draw(TextureRegion.split(WildGame.getAssetManager().getAsset("inventory_slot_equip"), 40, 40)[0][i-1], 20 + 6 * slotSize + 20, 20 + i * slotSize, slotSize, slotSize);
                } else {
                    sb.draw(WildGame.getAssetManager().getAsset("inventory_slot"), 20 + 6 * slotSize + 20, 20 + i * slotSize, slotSize, slotSize);
                    
                    if(draggingSlot != null && draggingSlot.isEquipped() && draggingSlot.getSlot() == i)
                        continue;
                    
                    if(getEquipped()[i] != null)
                        drawItemStack(sb, getEquipped()[i], 20 + 6 * slotSize + 20 + slotSize / 4, 30 + i * slotSize);
                }
            }
        }

        // Draw The Hotbar
        for(int i = 0; i < 8; i++)
            drawSlot(sb, i, Gdx.graphics.getWidth() / 2 - 8 / 2 * slotSize + i * slotSize, 0);

        // Draw Drag Thing
        if(inventoryOpen)
            if(draggingSlot != null)
                drawItemStack(sb, draggingSlot.isEquipped() ? getEquipped()[draggingSlot.getSlot()] : inventory[draggingSlot.getSlot()], Gdx.input.getX() - itemSize / 2, Gdx.graphics.getHeight() - (Gdx.input.getY() + itemSize / 2));
    }

    private void drawSlot(SpriteBatch sb, int slot, int x, int y) {
        int slotSize = (int) (40 * WildGame.getGUIScale());
    
        if(activeSlot == slot) {
            sb.draw(WildGame.getAssetManager().getAsset("inventory_slot_active"), x, y, slotSize, slotSize);
        } else {
            sb.draw(WildGame.getAssetManager().getAsset("inventory_slot"), x, y, slotSize, slotSize);
        }

        if(inventory[slot] == null)
            return;
        
        if(draggingSlot != null && !draggingSlot.isEquipped() && slot == draggingSlot.getSlot())
            return;

        drawItemStack(sb, inventory[slot], x + slotSize / 4, y + slotSize / 4);
    }
    
    private void drawItemStack(SpriteBatch sb, ItemStack stack, int x, int y) {
        int itemSize = (int) (20 * WildGame.getGUIScale());
        
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
            sb.draw(itemTexture, x, y, itemSize, itemSize);

        if(stack.getAmount() == 1)
            return;

        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_12");
        font.getData().setScale(WildGame.getGUIScale());
        GlyphLayout layout = new GlyphLayout(font, Integer.toString(stack.getAmount()));
        font.draw(sb, layout, x + itemSize * 1.2f - layout.width, y + itemSize / 4f);
    }
    
    private ItemStack[] getEquipped() {
        return ((WorldState) WildGame.getState()).getWorld().getLocalPlayer().getEquipped();
    }
    
    private Slot getSlot(int x, int y) {
        int slotSize = (int) (40 * WildGame.getGUIScale());
        
        int inv_x = (int) Math.floor((x - 20f) / slotSize);
        int inv_y = (int) Math.floor((y - 20f) / slotSize);
        if(inv_x >= 0 && inv_x < 6) {
            int slot = inv_y * 6 + inv_x;
            if(slot >= 0 && slot < inventory.length)
                return new Slot(false, 8 + slot);
        }
        
        if(x < 40 + slotSize * 7 && x > 40 + slotSize * 6)
            if(inv_y >= 0 && inv_y < getEquipped().length)
                return new Slot(true, inv_y);
            
        if(y > 0 && y <= slotSize) {
            int slot = (int) Math.floor((x - Gdx.graphics.getWidth() / 2f + 8f / 2f * slotSize) / slotSize);
            if(slot >= 0 && slot < 8)
                return new Slot(false, slot);
        }
        
        return null;
    }

    public int getActiveSlot() {
        return activeSlot;
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }
    
    public InputAdapter getInputAdapter() {
        return inputAdapter;
    }
    
    private static class Slot {
        
        private boolean equipped;
        private int slot;
        
        public Slot(boolean equipped, int slot) {
            this.equipped = equipped;
            this.slot = slot;
        }
    
        public boolean isEquipped() {
            return equipped;
        }
    
        public int getSlot() {
            return slot;
        }
        
    }
    
    public class InventoryInputAdapter extends InputAdapter {
    
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.TAB) {
                inventoryOpen = !inventoryOpen;
                return true;
            }
    
            int num = keycode - 8;
            if(num >= 0 && num < 8) {
                activeSlot = num;
                return true;
            }
            
            return false;
        }
    
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Slot slot = getSlot(screenX, Gdx.graphics.getHeight() - screenY);
            if(slot == null)
                return inventoryOpen;
            
            if(inventoryOpen)
                if(slot.isEquipped() ? getEquipped()[slot.getSlot()] != null : inventory[slot.getSlot()] != null)
                    draggingSlot = slot;
            
            if(!inventoryOpen && slot.getSlot() < 8) {
                activeSlot = slot.getSlot();
                return true;
            }
            
            return inventoryOpen;
        }
    
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            Slot droppedSlot = getSlot(screenX, Gdx.graphics.getHeight() - screenY);
            
            if(draggingSlot == null || droppedSlot == null)
                return inventoryOpen;
            
            if(!draggingSlot.isEquipped() && !droppedSlot.isEquipped()) {
                WildGame.getClient().sendTCP(new MoveItemSlotsPacket(draggingSlot.getSlot(), droppedSlot.getSlot()));
                
                ItemStack tempStack = inventory[draggingSlot.getSlot()];
                inventory[draggingSlot.getSlot()] = inventory[droppedSlot.getSlot()];
                inventory[droppedSlot.getSlot()] = tempStack;
            } else {
                EquipPacket packet = null;
                if(draggingSlot.isEquipped())
                    packet = new EquipPacket(droppedSlot.getSlot(), draggingSlot.getSlot());
                
                if(droppedSlot.isEquipped())
                    packet = new EquipPacket(draggingSlot.getSlot(), droppedSlot.getSlot());
                
                if(packet != null)
                    WildGame.getClient().sendTCP(packet);
            }
            
            draggingSlot = null;
            return inventoryOpen;
        }
        
    }
    
}
