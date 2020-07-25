package com.projectwild.game.pregame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.WildGame;
import com.projectwild.shared.packets.LoginResponsePacket;

import javax.swing.*;

public class LoginListener extends Listener {
    
    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof LoginResponsePacket) {
            LoginResponsePacket packet = (LoginResponsePacket) obj;
            JOptionPane.showMessageDialog(null, packet.getMessage());
            if(packet.isSuccess()) {
                WildGame.changeState(new WorldSelectionState());
            } else {
                WildGame.changeState(new LoginState());
            }
        }
    }
    
}
