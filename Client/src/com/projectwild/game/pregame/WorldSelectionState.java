package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.pregame.gui.PGUIParent;
import com.projectwild.game.pregame.gui.components.*;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.shared.packets.world.RequestWorldPacket;
import com.projectwild.shared.packets.world.RequestWorldResponsePacket;
import com.projectwild.shared.utils.Vector2;

public class WorldSelectionState implements GameState {

    private PGUIParent guiParent;
    
    private WorldSelectionListener worldSelectionListener;
    
    @Override
    public void initialize() {
        // Playing Music
        WildGame.getAssetManager().getSound("menu").resume();
        
        // Creating GUI Parent
        guiParent = new PGUIParent();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(guiParent.getInputAdapter());

        // Setting Up State
        worldSelectionListener = new WorldSelectionListener();
        WildGame.getClient().addListener(worldSelectionListener);

        WildGame.getDiscordIntegration().setPresence("In World Selection");

        // BG & Logo
        guiParent.addComponent(new Background(Color.valueOf("2a2a4d")));
        guiParent.addComponent(new Image(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 275), new Vector2(200), "logo128"));

        // World Selection
        TextField world = new TextField(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 400), 200, "World Name", Color.valueOf("56569c"), 10);
        guiParent.addComponent(world);

        Button enter = new Button(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 500), "Enter", Color.valueOf("56569c"));
        enter.setCallback(() -> {
            WildGame.getClient().sendTCP(new RequestWorldPacket(world.getText()));
        });
        guiParent.addComponent(enter);

        // Setup Key Shortcuts
        inputMultiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.ENTER)
                    WildGame.getClient().sendTCP(new RequestWorldPacket(world.getText()));
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    
    @Override
    public void update() {
    
    }
    
    @Override
    public void render() {
        guiParent.render();
    }
    
    @Override
    public void dispose() {
        WildGame.getClient().removeListener(worldSelectionListener);
        Gdx.input.setInputProcessor(null);
        guiParent.destroy();
    }
    
    public class WorldSelectionListener extends Listener {
        
        @Override
        public void received(Connection connection, Object obj) {
            if(obj instanceof RequestWorldResponsePacket) {
                RequestWorldResponsePacket packet = (RequestWorldResponsePacket) obj;
                if(packet.isSuccess()) {
                    WildGame.getAssetManager().getSound("menu").pause();
                    WildGame.changeState(new WorldState());
                } else {
                    guiParent.addComponent(new Notification(2, packet.getMessage(), Color.valueOf("56569c")));
                }
            }
        }
        
    }
    
}
