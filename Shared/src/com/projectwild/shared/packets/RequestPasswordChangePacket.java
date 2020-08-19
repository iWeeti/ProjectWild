package com.projectwild.shared.packets;

public class RequestPasswordChangePacket {
    
    private String currentPassword;
    private String password;
    
    public RequestPasswordChangePacket() {}
    
    public RequestPasswordChangePacket(String currentPassword, String password) {
        this.currentPassword = currentPassword;
        this.password = password;
    }
    
    public String getCurrentPassword() {
        return currentPassword;
    }
    
    public String getPassword() {
        return password;
    }
    
}
