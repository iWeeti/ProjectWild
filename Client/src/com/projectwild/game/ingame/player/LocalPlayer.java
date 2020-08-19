package com.projectwild.game.ingame.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.World;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.packets.player.PlayerAnimationPacket;
import com.projectwild.shared.packets.player.local.MovePacket;
import com.projectwild.shared.packets.world.LeaveWorldPacket;
import com.projectwild.shared.utils.Utils;
import com.projectwild.shared.utils.Vector2;

public class LocalPlayer extends Player {
    
    private PlayerInputAdapter inputAdapter;
    private boolean KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT;
    private long KEY_UP_TIME;

    private Vector2 oldVelocity, velocity;
    private float speedMultiplier;
    private boolean hasAccess;
    private int health;

    private boolean noclip;

    public LocalPlayer(int userId, String username) {
        super(userId, username);
        inputAdapter = new PlayerInputAdapter();
        speedMultiplier = 1.0f;
        hasAccess = false;
        velocity = new Vector2();
        noclip = false;
        health = 100;
    }

    public void handlePhysics() {
        World world = ((WorldState) WildGame.getState()).getWorld();

        // Completely Ignore Velocity If Noclipping
        if(noclip) {
            // Save Old Position
            Vector2 oldPos = getPosition().copy();

            if(KEY_LEFT)
                getPosition().changeX(-3 * speedMultiplier);

            if(KEY_RIGHT)
                getPosition().changeX(3 * speedMultiplier);

            if(KEY_UP)
                getPosition().changeY(3 * speedMultiplier);

            if(KEY_DOWN)
                getPosition().changeY(-3 * speedMultiplier);

            // Update The Server
            if(oldPos.getX() != getPosition().getX() || oldPos.getY() != getPosition().getY())
                WildGame.getClient().sendTCP(new MovePacket(oldPos, getPosition()));
            return;
        }

        // Save Old Velocity
        oldVelocity = velocity.copy();

        // First Off Check Keys
        if(KEY_LEFT)
            velocity.changeX(-2);

        if(KEY_RIGHT)
            velocity.changeX(2);

        // Make Player Go A Higher When Holding Jump
        if(KEY_UP && KEY_UP_TIME != -1) {
            double timeDown = (System.currentTimeMillis() - KEY_UP_TIME) / 1000.0;
            if(timeDown > 0.25)
                KEY_UP_TIME = -1;
            velocity.changeY((0.25 - timeDown) * 0.8);
        }

        // Enforce Terminal Velocity
        velocity.setX(Utils.clamp(5f * speedMultiplier, -5f * speedMultiplier, velocity.getX()));
        velocity.setY(Utils.clamp(5, -5, velocity.getY()));

        // Horizontal "Friction"
        velocity.changeX(-(velocity.getX() / 4f));
        if(Math.abs(velocity.getX()) < 0.01) // TODO: hotfix pls fix
            velocity.setX(0);

        // Gravity
        velocity.changeY(-0.2f);

        // Vertical Collision
        {
            // Define Predicted Position & Corners
            Vector2 newPos = getPosition().copy();
            newPos.changeY(velocity.getY());

            Vector2 topLeft = new Vector2(newPos.getX() + 8, newPos.getY() + 26);
            Vector2 topRight = new Vector2(newPos.getX() + 24, newPos.getY() + 26);
            Vector2 bottomLeft = new Vector2(newPos.getX() + 8, newPos.getY());
            Vector2 bottomRight = new Vector2(newPos.getX() + 24, newPos.getY());

            // Perform Collision Calculations
            Block block1 = world.pointCollisionBlock(velocity.getY() > 0 ? topLeft : bottomLeft);
            Block block2 = world.pointCollisionBlock(velocity.getY() > 0 ? topRight : bottomRight);

            if(block1 == null || block2 == null) {
                velocity.setY(0);
            } else {
                int type1 = block1.getBlockPreset().getCollisionType();
                int type2 = block2.getBlockPreset().getCollisionType();

                if(!block1.collide())
                    type1 = 0;

                if(!block2.collide())
                    type2 = 0;

                if(type1 == 1 || type2 == 1) {
                    velocity.setY(0);
                } else if((type1 == 2 || type2 == 2) && velocity.getY() < 0) {
                    if(world.pointCollisionBlock(getPosition().copy().changeX(8)) != block1)
                        velocity.setY(0);
                    if(world.pointCollisionBlock(getPosition().copy().changeX(24)) != block2)
                        velocity.setY(0);
                } else {
                    if(type1 == 3 || type2 == 3) {
                        velocity.changeY(0.15f);
                        velocity.setY(Utils.clamp(2, -2, velocity.getY()));
                    }
                }
            }
        }

        // Horizontal Collision
        {
            // Define Predicted Position & Corners
            Vector2 newPos = getPosition().copy();
            newPos.changeX(velocity.getX());
            newPos.changeY(velocity.getY());

            Vector2 topLeft = new Vector2(newPos.getX() + 8, newPos.getY() + 26);
            Vector2 topRight = new Vector2(newPos.getX() + 24, newPos.getY() + 26);
            Vector2 bottomLeft = new Vector2(newPos.getX() + 8, newPos.getY());
            Vector2 bottomRight = new Vector2(newPos.getX() + 24, newPos.getY());

            // Perform Collision Calculations
            Block block1 = world.pointCollisionBlock(velocity.getX() > 0 ? topRight : topLeft);
            Block block2 = world.pointCollisionBlock(velocity.getX() > 0 ? bottomRight : bottomLeft);

            if(block1 == null || block2 == null) {
                velocity.setX(0);
            } else {
                int type1 = block1.getBlockPreset().getCollisionType();
                int type2 = block2.getBlockPreset().getCollisionType();

                if(!block1.collide())
                    type1 = 0;

                if(!block2.collide())
                    type2 = 0;

                if(type1 == 1 || type2 == 1) {
                    velocity.setX(0);
                } else {
                    if(type1 == 3 || type2 == 3)
                        velocity.setX(Utils.clamp(2, -2, velocity.getX()));
                }
            }
        }

        // Save Old Position
        Vector2 oldPos = getPosition().copy();

        // Update Position
        getPosition().changeX(velocity.getX());
        getPosition().changeY(velocity.getY());

        // Update The Server
        if(oldPos.getX() != getPosition().getX() || oldPos.getY() != getPosition().getY())
            WildGame.getClient().sendTCP(new MovePacket(oldPos, getPosition()));
    }

    public void handleAnimation() {
        if(noclip)
            return;
        
        if(velocity.getX() > 0 && oldVelocity.getX() <= 0)
            changeAnimation(PlayerAnimations.WALK_RIGHT);

        if(velocity.getX() < 0 && oldVelocity.getX() >= 0)
            changeAnimation(PlayerAnimations.WALK_LEFT);

        if(velocity.getX() == 0 && oldVelocity.getX() != 0)
            changeAnimation(oldVelocity.getX() > 0 ? PlayerAnimations.STAND_RIGHT : PlayerAnimations.STAND_LEFT);

        if(velocity.getY() > 0 && oldVelocity.getY() <= 0) {
            changeAnimation(velocity.getX() > 0 ? PlayerAnimations.JUMP_RIGHT_START : PlayerAnimations.JUMP_LEFT_START);
        }

        if(velocity.getY() < 0 && oldVelocity.getY() >= 0) {
            changeAnimation(velocity.getX() > 0 ? PlayerAnimations.JUMP_RIGHT_END : PlayerAnimations.JUMP_LEFT_END);
        }

        if(velocity.getY() == 0 && oldVelocity.getY() != 0) {
            if(velocity.getX() == 0) {
                changeAnimation(getAnimation().getId() == PlayerAnimations.JUMP_RIGHT_END.getId() ? PlayerAnimations.STAND_RIGHT : PlayerAnimations.STAND_LEFT);
            } else {
                changeAnimation(velocity.getX() > 0 ? PlayerAnimations.WALK_RIGHT : PlayerAnimations.WALK_LEFT);
            }
        }
    }

    public boolean isOnGround() {
        World world = ((WorldState) WildGame.getState()).getWorld();

        Vector2 corner1 = getPosition().copy();
        corner1.changeX(8);
        corner1.changeY(-2);

        Vector2 corner2 = getPosition().copy();
        corner2.changeX(24);
        corner2.changeY(-2);

        Block block1 = world.pointCollisionBlock(corner1);
        Block block2 = world.pointCollisionBlock(corner2);

        if(block1 == null || block2 == null)
            return true;

        if((block1.getBlockPreset().getCollisionType() == 3 || block2.getBlockPreset().getCollisionType() == 3) && velocity.getY() < 0) {
            if(world.pointCollisionBlock(getPosition().copy().changeX(8)) != block1)
                return true;
            if(world.pointCollisionBlock(getPosition().copy().changeX(24)) != block2)
                return true;
            return false;
        }

        return block1.getBlockPreset().getCollisionType() != 0 || block2.getBlockPreset().getCollisionType() != 0;
    }

    public void changeAnimation(PlayerAnimations animation) {
        changeAnimation(animation.getId());
    }

    @Override
    public void changeAnimation(int animationId) {
        super.changeAnimation(animationId);
        WildGame.getClient().sendTCP(new PlayerAnimationPacket(-1, animationId));
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public void setNoclip(boolean noclip) {
        this.noclip = noclip;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public boolean isNoclip() {
        return noclip;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
    
    public PlayerInputAdapter getInputAdapter() {
        return inputAdapter;
    }
    
    public class PlayerInputAdapter extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            switch(keycode) {
                case Input.Keys.W:
                case Input.Keys.UP:
                case Input.Keys.SPACE:
                    if(isOnGround()) {
                        getVelocity().setY(4.0);
                       KEY_UP_TIME = System.currentTimeMillis();
                    }
                    KEY_UP = true;
                    break;
                case Input.Keys.S:
                case Input.Keys.DOWN:
                    KEY_DOWN = true;
                    break;
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    KEY_LEFT = true;
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    KEY_RIGHT = true;
                    break;
            }
        
            return false;
        }
    
        @Override
        public boolean keyUp(int keycode) {
            switch(keycode) {
                case Input.Keys.W:
                case Input.Keys.UP:
                case Input.Keys.SPACE:
                    KEY_UP = false;
                    KEY_UP_TIME = -1;
                    break;
                case Input.Keys.S:
                case Input.Keys.DOWN:
                    KEY_DOWN = false;
                    break;
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    KEY_LEFT = false;
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    KEY_RIGHT = false;
                    break;
            }
            return false;
        }
    }
}
