package com.projectwild.game.pregame;

import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.shared.packets.LoginDataPacket;

import javax.swing.*;

public class LoginState implements GameState {
    
    private LoginListener loginListener;
    
    @Override
    public void initialize() {
        loginListener = new LoginListener();
        WildGame.getClient().addListener(loginListener);
        
        boolean isRegistering = 1 == JOptionPane.showOptionDialog(null, "How would you like to proceed?", "Login / Register", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {"Login", "Register"}, null);
        String username = JOptionPane.showInputDialog("Enter a username");
        String password = JOptionPane.showInputDialog("Enter a password");
        
        WildGame.getClient().sendTCP(new LoginDataPacket(username, password, isRegistering));
    }
    
    @Override
    public void update() {
    
    }
    
    @Override
    public void render() {
    
    }
    
    @Override
    public void dispose() {
        WildGame.getClient().removeListener(loginListener);
    }
    
}
