package com.projectwild.shared.packets.world;

public class WorldDataPacket {

    private int width, height;
    private byte[] blockData;
    
    public WorldDataPacket() {}
    
    public WorldDataPacket(int width, int height, byte[] blockData) {
        this.width = width;
        this.height = height;
        this.blockData = blockData;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public byte[] getBlockData() {
        return blockData;
    }

}
