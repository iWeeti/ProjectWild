package com.projectwild.shared.packets.world;

public class RequestWorldPacket {
    
    private String world;
    
    public RequestWorldPacket() {}
    
    public RequestWorldPacket(String world) {
        this.world = world;
    }
    
    public String getWorld() {
        return world;
    }
    
}
