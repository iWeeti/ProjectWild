package com.projectwild.shared.packets.world;

public class InteractBlockPacket {

    private int slot;
    private int x, y, z;

    public InteractBlockPacket() {}

    public InteractBlockPacket(int x, int y, int z, int slot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.slot = slot;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getSlot() {
        return slot;
    }

}
