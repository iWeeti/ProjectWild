package com.projectwild.shared.packets.world;

public class InteractBlockPacket {

    private int x, y, z;

    public InteractBlockPacket() {}

    public InteractBlockPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

}
