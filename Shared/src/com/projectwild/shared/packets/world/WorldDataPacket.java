package com.projectwild.shared.packets.world;

public class WorldDataPacket {

    private String background;

    private int width, height;
    private byte[] blockData;
    
    public WorldDataPacket() {}
    
    public WorldDataPacket(int width, int height, byte[] blockData, String background) {
        this.width = width;
        this.height = height;
        this.blockData = blockData;
        this.background = background;
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

    public String getBackground() {
        return background;
    }

}
