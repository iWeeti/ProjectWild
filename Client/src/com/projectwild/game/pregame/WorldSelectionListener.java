package com.projectwild.game.pregame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.WildGame;
import com.projectwild.game.worlds.WorldState;
import com.projectwild.shared.packets.world.RequestWorldResponsePacket;
import com.projectwild.shared.packets.world.WorldDataPacket;

import javax.swing.*;

public class WorldSelectionListener extends Listener {
    
    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof RequestWorldResponsePacket) {
            RequestWorldResponsePacket packet = (RequestWorldResponsePacket) obj;
            if(packet.isSuccess()) {
                WildGame.changeState(new WorldState());
            } else {
                JOptionPane.showMessageDialog(null, packet.getMessage());
            }
        }
    }
    
}
