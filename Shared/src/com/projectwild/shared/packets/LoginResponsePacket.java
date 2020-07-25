package com.projectwild.shared.packets;

public class LoginResponsePacket {
    
    private boolean success;
    private String message;
    
    public LoginResponsePacket() {}
    
    public LoginResponsePacket(boolean success, String message) {
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
