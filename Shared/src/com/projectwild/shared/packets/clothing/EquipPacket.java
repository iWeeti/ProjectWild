package com.projectwild.shared.packets.clothing;

public class EquipPacket {

    public int invSlot;
    public int clothingSlot;

    public EquipPacket() {}

    public EquipPacket(int invSlot, int clothingSlot) {
        this.invSlot = invSlot;
        this.clothingSlot = clothingSlot;
    }

    public int getInvSlot() {
        return invSlot;
    }

    public int getClothingSlot() {
        return clothingSlot;
    }

}
