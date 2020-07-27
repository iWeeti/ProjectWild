package com.projectwild.shared.packets.items;

import com.projectwild.shared.ItemStack;

public class ChangeInventoryItemPacket {

    private int slot;
    private ItemStack itemStack;

    public ChangeInventoryItemPacket() {}

    public ChangeInventoryItemPacket(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
