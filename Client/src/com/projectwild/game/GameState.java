package com.projectwild.game;

public interface GameState {
    
    public void initialize();
    
    public void update();
    
    public void render();
    
    public void dispose();
    
}
