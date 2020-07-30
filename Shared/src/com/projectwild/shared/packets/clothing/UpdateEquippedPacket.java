package com.projectwild.shared.packets.clothing;

import com.projectwild.shared.ItemStack;

public class UpdateEquippedPacket {

    private int userId;
    private ItemStack[] equipped;

    public UpdateEquippedPacket() {}

    public UpdateEquippedPacket(int userId, ItemStack[] equipped) {
        this.userId = userId;
        this.equipped = equipped;
    }

    public int getUserId() {
        return userId;
    }

    public ItemStack[] getEquipped() {
        return equipped;
    }

}
