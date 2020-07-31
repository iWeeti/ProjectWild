package com.projectwild.game.pregame;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIParent;
import com.projectwild.game.gui.components.Notification;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.shared.packets.world.RequestWorldResponsePacket;

import javax.swing.*;

public class WorldSelectionListener extends Listener {

    private GUIParent guiParent;

    public WorldSelectionListener(GUIParent guiParent) {
        this.guiParent = guiParent;
    }

    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof RequestWorldResponsePacket) {
            RequestWorldResponsePacket packet = (RequestWorldResponsePacket) obj;
            if(packet.isSuccess()) {
                WildGame.changeState(new WorldState());
            } else {
                guiParent.addComponent(new Notification(2, packet.getMessage(), Color.valueOf("56569c")));
            }
        }
    }
    
}
