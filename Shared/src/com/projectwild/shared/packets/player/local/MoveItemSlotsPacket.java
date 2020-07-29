package com.projectwild.shared.packets.player.local;

public class MoveItemSlotsPacket {

    private int originSlot;
    private int targetSlot;

    public MoveItemSlotsPacket() {}

    public MoveItemSlotsPacket(int originSlot, int targetSlot) {
        this.originSlot = originSlot;
        this.targetSlot = targetSlot;
    }

    public int getOriginSlot() {
        return originSlot;
    }

    public int getTargetSlot() {
        return targetSlot;
    }

}
