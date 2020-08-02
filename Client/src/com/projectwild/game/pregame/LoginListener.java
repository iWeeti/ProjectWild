package com.projectwild.game.pregame;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.pregame.GUIParent;
import com.projectwild.game.gui.pregame.components.Notification;
import com.projectwild.shared.packets.LoginResponsePacket;

public class LoginListener extends Listener {

    private GUIParent guiParent;

    public LoginListener(GUIParent guiParent) {
        this.guiParent = guiParent;
    }

    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof LoginResponsePacket) {
            LoginResponsePacket packet = (LoginResponsePacket) obj;
            if(!packet.isSuccess()) {
                guiParent.addComponent(new Notification(2, packet.getMessage(), Color.valueOf("56569c")));
            } else {
                WildGame.changeState(new WorldSelectionState());
            }
        }
    }
    
}
