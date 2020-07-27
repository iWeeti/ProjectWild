package com.projectwild.shared;

public class ItemStack {

    private ItemPreset itemPreset;
    private int amount;

    public ItemStack() {}

    public ItemStack(ItemPreset itemPreset, int amount) {
        this.itemPreset = itemPreset;
        this.amount = amount;
    }

    public ItemPreset getItemPreset() {
        return itemPreset;
    }

    public int getAmount() {
        return amount;
    }

}
