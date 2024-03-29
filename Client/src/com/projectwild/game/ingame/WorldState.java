package com.projectwild.game.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.gui.GUIHandler;
import com.projectwild.game.ingame.gui.GUIWindow;
import com.projectwild.game.ingame.gui.components.*;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.packets.ChangePasswordResponsePacket;
import com.projectwild.shared.packets.RequestPasswordChangePacket;
import com.projectwild.shared.packets.player.RequestRespawnPacket;
import com.projectwild.shared.packets.world.InteractBlockPacket;
import com.projectwild.shared.packets.world.LeaveWorldPacket;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class WorldState implements GameState {

    private InventoryHandler inventoryHandler;
    private ChatHandler chatHandler;
    private GUIHandler guiHandler;

    private World world;
    private WorldListener listener;

    private InputMultiplexer inputMultiplexer;

    private SpriteBatch sb;
    private ShapeRenderer sr;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private OrthographicCamera guiCamera;

    private Sound backgroundSound;

    @Override
    public void initialize() {
        backgroundSound = WildGame.getAssetManager().getSound("birds_forest");
        backgroundSound.loop(0.35f);

        // Setting Up Handlers & Listeners
        inventoryHandler = new InventoryHandler();
        chatHandler = new ChatHandler();
        guiHandler = new GUIHandler();

        listener = new WorldListener(this);
        WildGame.getClient().addListener(listener);

        // Setting Up Rendering Stuff
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.6f;

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false);

        guiCamera = new OrthographicCamera();
        guiCamera.setToOrtho(true); // This Is Why This Camera Exists

        // Setting Up Input Stuff
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode != Input.Keys.ESCAPE)
                    return false;

                if(chatHandler.isChatOpen()) {
                    chatHandler.toggleChat();
                } else {
                    if(guiHandler.getWindow("pause") == null) {
                        guiHandler.createFromPreset("pause");
                    } else {
                        guiHandler.destroyWindow("pause");
                    }
                }
                return true;
            }
        });
        inputMultiplexer.addProcessor(guiHandler.getInputAdapter());
        inputMultiplexer.addProcessor(chatHandler.getTypeListener());
        inputMultiplexer.addProcessor(inventoryHandler.getInputAdapter());
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
                camera.zoom = Math.min(1.0f, Math.max(0.2f, camera.zoom + ((float) amount * 0.05f)));
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Check For HUD Interaction
                if(chatHandler.isChatOpen())
                    return false;

                // Handle Block Input
                Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                int x = (int) Math.floor(pos.x / 32.0f);
                int y = (int) Math.floor(pos.y / 32.0f);

                InteractBlockPacket packet = new InteractBlockPacket(x, y, 1, inventoryHandler.getActiveSlot());
                WildGame.getClient().sendTCP(packet);
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Registering Pause GUI Window
        guiHandler.registerPresetConstructor((args) -> {
            GUIButtonComponent backButton = new GUIButtonComponent("Back");
            backButton.setCallback(() -> {
                guiHandler.destroyWindow("pause");
            });

            GUIButtonComponent respawnButton = new GUIButtonComponent("Respawn");
            respawnButton.setCallback(() -> {
                WildGame.getClient().sendTCP(new RequestRespawnPacket());
                guiHandler.destroyWindow("pause");
            });
            
            GUIButtonComponent changePassword = new GUIButtonComponent("Change Password");
            changePassword.setCallback(() -> {
                guiHandler.createFromPreset("changepassword");
            });

            GUIButtonComponent settingsButton = new GUIButtonComponent("Settings");
            settingsButton.setCallback(() -> {
                guiHandler.destroyWindow("pause");
                guiHandler.createFromPreset("settings");
            });

            GUIButtonComponent exitButton = new GUIButtonComponent("Exit", new Color(244f / 255f, 85f / 255f, 85f / 255f, 1f));
            exitButton.setCallback(() -> {
                LeaveWorldPacket leaveWorldPacket = new LeaveWorldPacket();
                WildGame.getClient().sendTCP(leaveWorldPacket);
                WildGame.changeState(new WorldSelectionState());
            });

            return new GUIWindow.Builder("pause")
                    .add(false, backButton)
                    .add(true, respawnButton)
                    .add(true, changePassword)
                    .add(true, settingsButton)
                    .add(true, exitButton)
                    .build();
        });

        // Registering Settings GUI Window
        guiHandler.registerPresetConstructor((args) -> {
            GUIButtonComponent backButton = new GUIButtonComponent("Back");
            backButton.setCallback(() -> {
                guiHandler.destroyWindow("settings");
                guiHandler.createFromPreset("pause");
            });
            
            return new GUIWindow.Builder("settings")
                    .add(false, backButton)
                    .build();
        });
        
        // Registering Change Password GUI Window
        guiHandler.registerPresetConstructor((args) -> {
            Listener responseListener = new Listener() {
                @Override
                public void received(Connection connection, Object obj) {
                    if(obj instanceof ChangePasswordResponsePacket) {
                        ChangePasswordResponsePacket packet = (ChangePasswordResponsePacket) obj;
                        if(packet.isSuccess()) {
                            guiHandler.destroyWindow("changepassword");
                            guiHandler.createFromPreset("notification", "[GREEN]"+packet.getMessage());
                        } else {
                            guiHandler.createFromPreset("changepassword", "[RED]"+packet.getMessage());
                        }
                    }
                }
            };
            
            GUITextComponent header = new GUITextComponent("Change Password", GUITextComponent.Fonts.HEADER);
            
            GUIInputComponent oldPassword = new GUIInputComponent("Old Password", 16, true);
            GUIInputComponent newPassword = new GUIInputComponent("New Password", 16, true);
            GUIInputComponent newPasswordConfirm = new GUIInputComponent("Repeat Password", 16, true);
            
            GUIButtonComponent confirmButton = new GUIButtonComponent("Change Password");
            confirmButton.setCallback(() -> {
                if(!newPassword.getValue().equals(newPasswordConfirm.getValue())) {
                    guiHandler.createFromPreset("changepassword", "[RED]Passwords Don't Match.");
                    return;
                }
                WildGame.getClient().addListener(responseListener);
                WildGame.getClient().sendTCP(new RequestPasswordChangePacket(oldPassword.getValue(), newPassword.getValue()));
            });
            
            GUIButtonComponent cancelButton = new GUIButtonComponent("Cancel");
            cancelButton.setCallback(() -> {
                guiHandler.destroyWindow("changepassword");
            });
            
            return new GUIWindow.Builder("changepassword")
                    .add(false, header, GUIWindow.Builder.Align.LEFT)
                    .add(true, args.length > 0 ? new GUITextComponent((String) args[0]): null, GUIWindow.Builder.Align.LEFT)
                    .add(true, oldPassword, GUIWindow.Builder.Align.LEFT)
                    .add(true, new GUISpacerComponent(10, 1), GUIWindow.Builder.Align.LEFT)
                    .add(true, newPassword, GUIWindow.Builder.Align.LEFT)
                    .add(true, newPasswordConfirm, GUIWindow.Builder.Align.LEFT)
                    .add(true, confirmButton, GUIWindow.Builder.Align.LEFT)
                    .add(false, cancelButton, GUIWindow.Builder.Align.LEFT)
                    .onDispose(() -> {
                        WildGame.getClient().removeListener(responseListener);
                    })
                    .build();
        });
        
        // Registering Notification Window
        guiHandler.registerPresetConstructor("notification", (args) -> {
            String text = "Empty Notification.";
            if(args.length > 0)
                text = (String) args[0];
    
            // Generates Random ID
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            String id = new String(array, StandardCharsets.UTF_8).toLowerCase();
    
            GUITextComponent message = new GUITextComponent(text);
            GUIButtonComponent closeButton = new GUIButtonComponent("Close");
            closeButton.setCallback(() -> {
                guiHandler.destroyWindow(id+"_notification");
            });
    
            return new GUIWindow.Builder(id+"_notification")
                    .add(false, message)
                    .add(true, closeButton)
                    .build();
        });
    }

    @Override
    public void update() {
        if(world == null)
            return;

        if(world.getLocalPlayer() == null)
            return;

        world.getLocalPlayer().handlePhysics();
        world.getLocalPlayer().handleAnimation();
    }

    @Override
    public void render() {
        if(world == null || world.getLocalPlayer() == null)
            return;

        // Update camera position
        double plyPosX = world.getLocalPlayer().getPosition().getX()+16;
        double plyPosY = world.getLocalPlayer().getPosition().getY()+16;
        double camPosX = Math.max((camera.viewportWidth/2)*camera.zoom, Math.min(plyPosX, world.getWidth()*32-(camera.viewportWidth/2)*camera.zoom));
        double camPosY = Math.max((camera.viewportHeight/2)*camera.zoom, Math.min(plyPosY, world.getHeight()*32-(camera.viewportHeight/2)*camera.zoom));
        camera.position.x += (camPosX - camera.position.x) * .3f;
        camera.position.y += (camPosY - camera.position.y) * .3f;
        camera.update();

        sb.begin();

        // Render World
        sb.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
        world.renderWorld(sb, sr, camera);

        // Render HUD
        sr.setProjectionMatrix(hudCamera.combined);
        sb.setProjectionMatrix(hudCamera.combined);
        inventoryHandler.render(sb, sr);
        chatHandler.render(sb);

        {
            // Health Rendering
            int size = (int) (24 * WildGame.getGUIScale());
            int hearts = (int) Math.floor(world.localPlayer.getHealth() / 10f);
            int x = size / 5;
            for(int i = 0; i < hearts; i++) {
                sb.draw(WildGame.getAssetManager().getAsset("heart"), x, Gdx.graphics.getHeight() - size * 1.2f, size, size);
                x += size * 1.1f;
            }
        }

        // Render GUI
        sr.setProjectionMatrix(guiCamera.combined);
        sb.setProjectionMatrix(guiCamera.combined);
        guiHandler.render(sb, sr);

        sb.end();
    }

    @Override
    public void dispose() {
        WildGame.getClient().removeListener(listener);
        Gdx.input.setInputProcessor(null);
        backgroundSound.stop();
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public GUIHandler getGuiHandler() {
        return guiHandler;
    }

}
