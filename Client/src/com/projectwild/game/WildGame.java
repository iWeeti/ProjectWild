package com.projectwild.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.kryonet.Client;
import com.projectwild.game.pregame.ConnectingState;
import com.projectwild.game.pregame.LoadingState;
import com.projectwild.shared.PacketRegistry;

import java.io.IOException;

public class WildGame extends ApplicationAdapter {

    private static Client client;
    private static GameState tempState, currentState;
    private static AssetManager assetManager;
    private static DiscordIntegration discordIntegration;
    
    public static void main(String[] args) {
        // Only Done On Desktop
        discordIntegration = new DiscordIntegration();
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1920;
        config.height = 1080;
        config.resizable = false;
        config.vSyncEnabled = true;
        config.addIcon("data/assets/logo32.png", Files.FileType.Internal);
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
        
        // Creating / Setting Up Network Client
        client = new Client(10000000, 10000000);
        PacketRegistry.register(client.getKryo());
        client.start();
    
        String address = System.getenv("address") == null ? "104.248.65.87" : System.getenv("address");
    
        // always try to reconnect if the client is disconnected.
        Thread reconnectThread = new Thread(() -> {
            // loop indefinitely
            while (true){
                // client disconnected
                if (!client.isConnected()) {
                    try {
                        // connect
                        client.connect(5000, address, 7707, 7707);
                    
                        // if connected switch to loading state
                        changeState(new LoadingState());
                    } catch (IOException e) {
                        changeState(new ConnectingState());
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        reconnectThread.start();
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

        if (currentState != null){
            currentState.update();
            currentState.render();
        }
    }
    
    @Override
    public void dispose() {
        currentState.dispose();
    }
    
    public static float getGUIScale() {
        return Gdx.graphics.getWidth() / 1280f;
    }
    
}
