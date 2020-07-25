package com.projectwild.shared.packets.world;

public class UpdateBlockPacket {

    private int x, y, z;
    private int blockPresetId;
    private byte[] extraData;

    public UpdateBlockPacket() {}

    public UpdateBlockPacket(int x, int y, int z, int blockPresetId, byte[] extraData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockPresetId = blockPresetId;
        this.extraData = extraData;
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

    public int getBlockPresetId() {
        return blockPresetId;
    }

    public byte[] getExtraData() {
        return extraData;
    }

}
