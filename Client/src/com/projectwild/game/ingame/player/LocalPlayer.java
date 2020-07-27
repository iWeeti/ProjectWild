package com.projectwild.game.ingame.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.World;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.game.ingame.blocks.Block;
import com.projectwild.shared.packets.player.PlayerAnimationPacket;
import com.projectwild.shared.packets.player.local.MovePacket;
import com.projectwild.shared.utils.Utils;
import com.projectwild.shared.utils.Vector2;

public class LocalPlayer extends Player {

    private Vector2 velocity, oldVelocity;
    private float speedMultiplier;
    private boolean hasAccess;

    public LocalPlayer(int userId, String username) {
        super(userId, username);
        speedMultiplier = 1.0f;
        hasAccess = false;
        velocity = new Vector2();
        oldVelocity = new Vector2();
    }

    public void handleInput() {
        // Save Velocity As Old Velocity
        oldVelocity.set(velocity);

        if(((WorldState) WildGame.getState()).getChatHandler().isChatOpen())
            return;

        // Handle Input
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            velocity.changeX(-2 * speedMultiplier);

        if(Gdx.input.isKeyPressed(Input.Keys.D))
            velocity.changeX(2 * speedMultiplier);

        if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W)) && isOnGround())
            velocity.changeY(5);
    }

    public void handlePhysics() {
        World world = ((WorldState) WildGame.getState()).getWorld();

        // Save Old Position
        Vector2 oldPos = getPosition().copy();

        // Define Terminal Velocity
        velocity.setX(Utils.clamp(5  * speedMultiplier, -5 * speedMultiplier, velocity.getX()));
        velocity.setY(Utils.clamp(5  * speedMultiplier, -5 * speedMultiplier, velocity.getY()));

        // Apply Horizontal "Friction"
        velocity.changeX((velocity.getX() > 0 ? -1 : 1) * Math.abs(velocity.getX()) / 3);

        if(Math.abs(velocity.getX()) < 0.01)
            velocity.setX(0);

        // Apply "Gravity"
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

                if(type1 == 1 || type2 == 1) {
                    velocity.setY(0);
                } else {
                    if(type1 == 2)
                        velocity.setY(block1.collide(this, velocity.getY()));
                    if(type2 == 2)
                        velocity.setY(block2.collide(this, velocity.getY()));
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

                if(type1 == 1 || type2 == 1) {
                    velocity.setX(0);
                } else {
                    if(type1 == 2)
                        velocity.setX(block1.collide(this, velocity.getX()));
                    if(type2 == 2)
                        velocity.setX(block2.collide(this, velocity.getX()));
                }
            }
        }

        // Update Position
        getPosition().changeX(velocity.getX());
        getPosition().changeY(velocity.getY());

        // Update The Server
        if(oldPos.getX() != getPosition().getX() || oldPos.getY() != getPosition().getY()) {
            WildGame.getClient().sendTCP(new MovePacket(oldPos, getPosition()));
        }
    }

    public void handleAnimation() {
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

        return block1.getBlockPreset().getCollisionType() == 1 || block2.getBlockPreset().getCollisionType() == 1;
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

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

}
