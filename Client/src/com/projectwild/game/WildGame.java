package com.projectwild.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.kryonet.Client;
import com.projectwild.game.pregame.LoadingState;
import com.projectwild.game.pregame.LoginState;
import com.projectwild.shared.PacketRegistry;

import java.io.IOException;

public class WildGame extends ApplicationAdapter {

    private static Client client;
    private static GameState tempState, currentState;
    private static AssetManager assetManager;
    private static DiscordIntegration discordIntegration;
    
    public static void main(String[] args) throws IOException {
        discordIntegration = new DiscordIntegration();

        client = new Client(10000000, 10000000);
        PacketRegistry.register(client.getKryo());
        client.start();
        client.connect(5000, "78.27.101.11", 7707, 7707);
        
        changeState(new LoadingState());
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        config.vSyncEnabled = true;
        new LwjglApplication(new WildGame(), config);
    }
    
    public static void changeState(GameState state) {
        tempState = state;
    }
    
    public static GameState getState() {
        return currentState;
    }
    
    public static Client getClient() {
        return client;
    }
    
    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public static DiscordIntegration getDiscordIntegration() {
        return discordIntegration;
    }

    @Override
    public void create() {
        assetManager = new AssetManager();
    }
    
    @Override
    public void render() {
        Gdx.graphics.setTitle("Project Wild - FPS: " + Gdx.graphics.getFramesPerSecond());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(tempState != null) {
            if(currentState != null)
                currentState.dispose();
            currentState = tempState;
            tempState = null;
            currentState.initialize();
        }
        
        currentState.update();
        currentState.render();
    }
    
    @Override
    public void dispose() {
        currentState.dispose();
    }
    
}
