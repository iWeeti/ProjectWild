package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIParent;
import com.projectwild.game.gui.components.Background;
import com.projectwild.game.gui.components.Button;
import com.projectwild.game.gui.components.Image;
import com.projectwild.game.gui.components.TextField;
import com.projectwild.shared.packets.LoginDataPacket;
import com.projectwild.shared.packets.world.RequestWorldPacket;
import com.projectwild.shared.utils.Vector2;

import javax.swing.*;

public class WorldSelectionState implements GameState {

    private GUIParent guiParent;
    
    private WorldSelectionListener worldSelectionListener;
    
    @Override
    public void initialize() {
        worldSelectionListener = new WorldSelectionListener();
        WildGame.getClient().addListener(worldSelectionListener);

        WildGame.getDiscordIntegration().setPresence("In World Selection", null);

        // Creating GUI Parent
        guiParent = new GUIParent();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(guiParent.getInputAdapter());

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
    
}
