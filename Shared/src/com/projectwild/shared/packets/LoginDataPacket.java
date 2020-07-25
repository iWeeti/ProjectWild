package com.projectwild.shared.packets;

public class LoginDataPacket {
    
    private String username, password;
    private boolean isRegistering;
    
    public LoginDataPacket() {}
    
    public LoginDataPacket(String username, String password, boolean isRegistering) {
        this.username = username;
        this.password = password;
        this.isRegistering = isRegistering;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean isRegistering() {
        return isRegistering;
    }
    
}
