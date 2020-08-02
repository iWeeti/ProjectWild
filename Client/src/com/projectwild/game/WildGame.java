package com.projectwild.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.projectwild.game.pregame.ConnectingState;
import com.projectwild.game.pregame.LoadingState;
import com.projectwild.game.pregame.LoginState;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.PacketRegistry;

import java.io.IOException;
import java.time.Clock;

public class WildGame extends ApplicationAdapter {

    private static Client client;
    private static GameState tempState, currentState;
    private static AssetManager assetManager;
    private static DiscordIntegration discordIntegration;
    private static Sound menuMusic;
    
    public static void main(String[] args) throws IOException {
        discordIntegration = new DiscordIntegration();

        client = new Client(10000000, 10000000);
        PacketRegistry.register(client.getKryo());
        client.start();

        connectLoop();

        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        config.vSyncEnabled = true;
        config.addIcon("data/assets/logo32.png", Files.FileType.Internal);
        new LwjglApplication(new WildGame(), config);
    }

    private static void connectLoop(){
        String address = System.getenv("address");
        if (address == null)
            address =  "104.248.65.87";

        // always try to reconnect if the client is disconnected.
        String finalAddress = address;
        Thread reconnectThread = new Thread(() -> {
            // loop indefinitely
            while (true){
                // client disconnected
                if (!client.isConnected()) {
                    try {
                        // connect
                        client.connect(5000, finalAddress, 7707, 7707);
                        // if connected switch to loading state
                        changeState(new LoadingState());
                    } catch (IOException e) {
                        changeState(new ConnectingState());
                        // failed to connect so wait 5 seconds and retry
                        System.out.println("Failed to connect, trying again in 5 seconds.");
                        if (currentState instanceof LoginState){
                            LoginState state = (LoginState) currentState;
                            state.addNotification("Failed to connect, trying again in 5 seconds.");
                        }
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

        if (currentState != null && (currentState.getClass() == LoginState.class || currentState.getClass() == WorldSelectionState.class)){
            if (menuMusic == null){
                menuMusic = assetManager.getSound("menu");
                menuMusic.loop(0.25f);
            } else {
                menuMusic.resume();
            }
        } else {
            if (menuMusic != null)
                menuMusic.pause();
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

    public static boolean isConnected() {
        return client.isConnected();
    }
    
}
