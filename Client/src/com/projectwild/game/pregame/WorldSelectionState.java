package com.projectwild.game.pregame;

import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.shared.packets.world.RequestWorldPacket;

import javax.swing.*;

public class WorldSelectionState implements GameState {
    
    private WorldSelectionListener worldSelectionListener;
    
    @Override
    public void initialize() {
        worldSelectionListener = new WorldSelectionListener();
        WildGame.getClient().addListener(worldSelectionListener);
    
        String world = JOptionPane.showInputDialog("What world would you like to enter?");
        WildGame.getClient().sendTCP(new RequestWorldPacket(world));
    }
    
    @Override
    public void update() {
    
    }
    
    @Override
    public void render() {
    
    }
    
    @Override
    public void dispose() {
        WildGame.getClient().removeListener(worldSelectionListener);
    }
    
}
