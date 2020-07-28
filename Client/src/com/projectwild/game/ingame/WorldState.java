package com.projectwild.game.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.player.Player;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.world.InteractBlockPacket;
import com.projectwild.shared.packets.world.LeaveWorldPacket;

public class WorldState implements GameState {

    private InventoryHandler inventoryHandler;
    private ChatHandler chatHandler;

    private World world;
    private WorldListener listener;

    private InputMultiplexer inputMultiplexer;

    private SpriteBatch sb;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    @Override
    public void initialize() {
        // Setting Up Handlers & Listeners
        inventoryHandler = new InventoryHandler();
        chatHandler = new ChatHandler();

        listener = new WorldListener(this);
        WildGame.getClient().addListener(listener);

        // Setting Up Rendering Stuff
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.6f;

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false);

        // Setting Up Input Stuff
        inputMultiplexer = new InputMultiplexer();
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

                if(inventoryHandler.handleMouseInput(screenX, screenY))
                    return false;

                // Handle Block Input
                Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                int x = (int) Math.floor(pos.x / 32.0f);
                int y = (int) Math.floor(pos.y / 32.0f);

                InteractBlockPacket packet = new InteractBlockPacket(x, y, 1, inventoryHandler.getActiveSlot());
                WildGame.getClient().sendTCP(packet);
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.ESCAPE) {
                    if(chatHandler.isChatOpen()) {
                        chatHandler.toggleChat();
                    } else {
                        LeaveWorldPacket leaveWorldPacket = new LeaveWorldPacket();
                        WildGame.getClient().sendTCP(leaveWorldPacket);
                        WildGame.changeState(new WorldSelectionState());
                    }
                }

                if(chatHandler.isChatOpen())
                    return false;

                switch(keycode) {
                    case Input.Keys.W:
                    case Input.Keys.SPACE:
                        if(world.getLocalPlayer().isOnGround())
                            world.getLocalPlayer().getVelocity().setY(5.0);
                        world.getLocalPlayer().KEY_UP = true;
                        break;
                    case Input.Keys.A:
                    case Input.Keys.LEFT:
                        world.getLocalPlayer().KEY_LEFT = true;
                        break;
                    case Input.Keys.D:
                    case Input.Keys.RIGHT:
                        world.getLocalPlayer().KEY_RIGHT = true;
                        break;
                }

                if(chatHandler.isChatOpen())
                    return false;

                if(inventoryHandler.handleKeyInput(keycode))
                    return false;

                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch(keycode) {
                    case Input.Keys.W:
                    case Input.Keys.SPACE:
                        world.getLocalPlayer().KEY_UP = false;
                        break;
                    case Input.Keys.A:
                    case Input.Keys.LEFT:
                        world.getLocalPlayer().KEY_LEFT = false;
                        break;
                    case Input.Keys.D:
                    case Input.Keys.RIGHT:
                        world.getLocalPlayer().KEY_RIGHT = false;
                        break;
                }
                return false;
            }
        });
        inputMultiplexer.addProcessor(chatHandler.getTypeListener());
        Gdx.input.setInputProcessor(inputMultiplexer);
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
        if(world == null)
            return;

        if(world.getLocalPlayer() == null)
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
        world.renderWorld(sb);

        // Render HUD
        sb.setProjectionMatrix(hudCamera.combined);
        inventoryHandler.render(sb);
        chatHandler.render(sb);

        sb.end();
    }
    
    @Override
    public void dispose() {
        WildGame.getClient().removeListener(listener);
        Gdx.input.setInputProcessor(null);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

}
