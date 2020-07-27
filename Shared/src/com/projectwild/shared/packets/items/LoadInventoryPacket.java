package com.projectwild.shared.packets.items;

import com.projectwild.shared.ItemStack;

public class LoadInventoryPacket {

    private ItemStack[] inventory;

    public LoadInventoryPacket() {}

    public LoadInventoryPacket(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

}
