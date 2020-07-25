package com.projectwild.game.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.worlds.player.Player;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.world.InteractBlockPacket;

public class WorldState implements GameState {

    private InputMultiplexer inputMultiplexer;
    private WorldListener listener;

    private World world;
    
    private SpriteBatch sb;
    private OrthographicCamera camera;
    
    @Override
    public void initialize() {
        listener = new WorldListener(this);
        WildGame.getClient().addListener(listener);
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.6f;

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
                camera.zoom = Math.min(1.0f, Math.max(0.2f, camera.zoom + ((float) amount * 0.05f)));
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                int x = (int) Math.floor(pos.x / 32.0f);
                int y = (int) Math.floor(pos.y / 32.0f);

                InteractBlockPacket packet = new InteractBlockPacket(x, y, 1);
                WildGame.getClient().sendTCP(packet);
                return false;
            }

        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    
    @Override
    public void update() {
        if(world == null)
            return;

        if(world.getLocalPlayer() == null)
            return;

        world.getLocalPlayer().handleInput();
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
        sb.setProjectionMatrix(camera.combined);

        sb.begin();
        for(int y = 0; y < world.getHeight(); y++) {
            for(int x = 0; x < world.getWidth(); x++) {
                for(int z = 0; z < 2; z++) {
                    BlockPreset preset = world.getBlocks()[y][x][z].getBlockPreset();

                    TextureRegion tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX(), preset.getTilesetY());
                    switch(preset.getRenderType()) {
                        case 1:
                            if(y == world.getBlocks().length-1)
                                break;
                            if(world.getBlocks()[y+1][x][z].getBlockPreset().getId() == preset.getId())
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX()+1, preset.getTilesetY());
                            break;
                        default:
                            break;
                    }
                    sb.draw(tex, x*32, y*32);
                }
            }
        }

        for(Player ply : world.getPlayers()) {
            ply.render(sb);
        }
        world.getLocalPlayer().render(sb);

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

}
