package com.projectwild.game.ingame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.ingame.blocks.BlockTypes;
import com.projectwild.game.ingame.player.LocalPlayer;
import com.projectwild.game.ingame.player.Player;
import com.projectwild.shared.BlockPreset;
import com.projectwild.shared.packets.world.WorldDataPacket;
import com.projectwild.shared.utils.Vector2;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {

    public CopyOnWriteArrayList<Player> players;

    public LocalPlayer localPlayer;

    private String background;

    private Block[][][] blocks;
    private int width, height;

    public World(WorldDataPacket dataPacket) {
        players = new CopyOnWriteArrayList<>();

        background = dataPacket.getBackground();

        width = dataPacket.getWidth();
        height = dataPacket.getHeight();

        blocks = new Block[height][width][2];
        ByteBuffer buffer = ByteBuffer.wrap(dataPacket.getBlockData());
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                for(int z = 0; z < 2; z++) {
                    int id = buffer.getInt();
                    byte[] extra = new byte[buffer.getInt()];
                    for(int i = 0; i < extra.length; i++) {
                        extra[i] = buffer.get();
                    }
                    try {
                        BlockPreset preset = BlockPreset.getPreset(id);
                        Class<? extends Block> blockClass = BlockTypes.getBlockClass(preset.getBlockType());
                        Block block = blockClass.getConstructor(BlockPreset.class, byte[].class, int.class, int.class, int.class).newInstance(preset, extra, x, y, z);
                        blocks[y][x][z] = block;
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void renderWorld(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
        // Render Background
        {
            Texture bg = WildGame.getAssetManager().getAsset("background");
            Texture bgTexture = WildGame.getAssetManager().getAsset(background);

            float scrollAmount = 1.5f;

            float textureWidth = camera.viewportWidth * scrollAmount;
            float textureHeight = camera.viewportHeight * scrollAmount;

            int wX = getWidth() * 16;
            int wY = getHeight() * 16;

            double cX = camera.position.x;
            double cY = camera.position.y;

            double rX = (wX - cX) / wX;
            double rY = (wY - cY) / wY;

            double offsetX = rX * (camera.viewportWidth / scrollAmount / 2);
            double offsetY = rY * (camera.viewportHeight / scrollAmount / 2);

            double x = (cX - textureWidth / 2) + offsetX;
            double y = (cY - textureHeight / 2) + offsetY;

            sb.draw(bg, (float) x, (float) y, textureWidth, textureHeight);
            sb.draw(bgTexture, (float) x, (float) y, textureWidth, textureHeight);
        }
        
        // Render Shadows
        sb.setColor(0, 0, 0, 0.3f);
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < 2; z++) {
                    BlockPreset preset = blocks[y][x][z].getBlockPreset();
                
                    TextureRegion tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX(), preset.getTilesetY());
                    switch(preset.getRenderType()) {
                        case 0:
                        case 3:
                            break;
                        case 1:
                            if(y == blocks.length-1)
                                break;
                            if(blocks[y+1][x][z].getBlockPreset().getId() == preset.getId())
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX()+1, preset.getTilesetY());
                            break;
                        case 2:
                        
                            break;
                        case 4:
                            blocks[y][x][z].renderShadow(sb, sr);
                            continue;
                        case 5:
                            boolean hasLeft = false;
                            boolean hasRight = false;
                            if(x > 0)
                                hasLeft = blocks[y][x-1][z].getBlockPreset().getId() == preset.getId();
                        
                            if(x < getBlocks()[0].length-1)
                                hasRight = blocks[y][x+1][z].getBlockPreset().getId() == preset.getId();
                        
                            if(hasLeft && hasRight) {
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 2, preset.getTilesetY());
                            } else {
                                if (hasLeft)
                                    tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 3, preset.getTilesetY());
                                if (hasRight)
                                    tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 1, preset.getTilesetY());
                            }
                            break;
                        case 6:
                            if(y == blocks.length-1)
                                break;
                            if(blocks[y+1][x][z].getBlockPreset().getId() == preset.getId()) {
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 1, preset.getTilesetY());
                            } else {
                                break;
                            }
                        
                            if(y == blocks.length-2)
                                break;
                            if(blocks[y+2][x][z].getBlockPreset().getId() == preset.getId())
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX()+2, preset.getTilesetY());
                            break;
                        default:
                            continue;
                    }
                    sb.draw(tex, x*32 + 2, y*32 - 2);
                }
            }
        }
        sb.setColor(1, 1, 1, 1);
    
        // Render Blocks
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < 2; z++) {
                    BlockPreset preset = blocks[y][x][z].getBlockPreset();

                    TextureRegion tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX(), preset.getTilesetY());
                    switch(preset.getRenderType()) {
                        case 0:
                            break;
                        case 1:
                            if(y == blocks.length-1)
                                break;
                            if(blocks[y+1][x][z].getBlockPreset().getId() == preset.getId())
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX()+1, preset.getTilesetY());
                            break;
                        case 2:

                            break;
                        case 4:
                            blocks[y][x][z].render(sb, sr);
                            continue;
                        case 5:
                            boolean hasLeft = false;
                            boolean hasRight = false;
                            if(x > 0)
                                hasLeft = blocks[y][x-1][z].getBlockPreset().getId() == preset.getId();

                            if(x < getBlocks()[0].length-1)
                                hasRight = blocks[y][x+1][z].getBlockPreset().getId() == preset.getId();

                            if(hasLeft && hasRight) {
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 2, preset.getTilesetY());
                            } else {
                                if (hasLeft)
                                    tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 3, preset.getTilesetY());
                                if (hasRight)
                                    tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 1, preset.getTilesetY());
                            }
                            break;
                        case 6:
                            if(y == blocks.length-1)
                                break;
                            if(blocks[y+1][x][z].getBlockPreset().getId() == preset.getId()) {
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX() + 1, preset.getTilesetY());
                            } else {
                                break;
                            }

                            if(y == blocks.length-2)
                                break;
                            if(blocks[y+2][x][z].getBlockPreset().getId() == preset.getId())
                                tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX()+2, preset.getTilesetY());
                            break;
                        default:
                            continue;
                    }
                    sb.draw(tex, x*32, y*32);
                }
            }
        }

        // Render Players
        for(Player ply : players) {
            ply.render(sb);
        }
        localPlayer.render(sb);

        // Render Blocks In Front Of Players
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < 2; z++) {
                    BlockPreset preset = blocks[y][x][z].getBlockPreset();
                    if(preset.getRenderType() != 3)
                        continue;

                    TextureRegion tex = WildGame.getAssetManager().getTile(preset.getTileset(), preset.getTilesetX(), preset.getTilesetY());
                    sb.draw(tex, x*32, y*32);
                }
            }
        }
    }

    public Block pointCollisionBlock(Vector2 point) {
        int blockX = (int) Math.floor((point.getX()) / 32.0f);
        int blockY = (int) Math.floor((point.getY()) / 32.0f);

        if(blockX >= getWidth() || blockX < 0 || blockY >= getHeight() || blockY < 0)
            return null;

        return blocks[blockY][blockX][1];
    }

    public Player createPlayer(int userId, String username) {
        Player player = new Player(userId, username);
        players.add(player);
        return player;
    }

    public void removePlayer(int userId) {
        for(Player ply : players) {
            if(ply.getUserId() == userId)
                players.remove(ply);
        }
    }

    public LocalPlayer createLocalPlayer(int userId, String username) {
        localPlayer = new LocalPlayer(userId, username);
        ((WorldState) WildGame.getState()).getInputMultiplexer().addProcessor(localPlayer.getInputAdapter());
        return localPlayer;
    }

    public Player getPlayer(int userId) {
        if(localPlayer.getUserId() == userId)
            return localPlayer;

        for(Player ply : players) {
            if(ply.getUserId() == userId)
                return ply;
        }
        return null;
    }

    public LocalPlayer getLocalPlayer() {
        return localPlayer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public String getBackground() {
        return background;
    }

}
