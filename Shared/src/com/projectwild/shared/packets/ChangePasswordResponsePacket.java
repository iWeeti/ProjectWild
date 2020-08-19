package com.projectwild.shared.packets;

public class ChangePasswordResponsePacket {
    
    private boolean success;
    private String message;
    
    public ChangePasswordResponsePacket() {}
    
    public ChangePasswordResponsePacket(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
}
