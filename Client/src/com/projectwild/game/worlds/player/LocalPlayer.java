package com.projectwild.game.worlds.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.projectwild.game.WildGame;
import com.projectwild.game.worlds.World;
import com.projectwild.game.worlds.WorldState;
import com.projectwild.shared.packets.player.PlayerAnimationPacket;
import com.projectwild.shared.packets.player.local.MovePacket;
import com.projectwild.shared.utils.Utils;
import com.projectwild.shared.utils.Vector2;

public class LocalPlayer extends Player {

    private Vector2 velocity, oldVelocity;
    private float speedMultiplier;

    //TODO: use speed multiplier

    public LocalPlayer(int userId, String username) {
        super(userId, username);
        speedMultiplier = 1.0f;
        velocity = new Vector2();
        oldVelocity = new Vector2();
    }

    public void handleInput() {
        // Save Velocity As Old Velocity
        oldVelocity.set(velocity);

        // Handle Input
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            velocity.changeX(-2);

        if(Gdx.input.isKeyPressed(Input.Keys.D))
            velocity.changeX(2);

        if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W)) && isOnGround())
            velocity.changeY(5);
    }

    public void handlePhysics() {
        World world = ((WorldState) WildGame.getState()).getWorld();

        // Save Old Position
        Vector2 oldPos = getPosition().copy();

        // Define Terminal Velocity
        velocity.setX(Utils.clamp(5, -5, velocity.getX()));
        velocity.setY(Utils.clamp(5, -5, velocity.getY()));

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
            if(world.pointCollisionType(velocity.getY() > 0 ? topLeft : bottomLeft) == 1 ||
                    world.pointCollisionType(velocity.getY() > 0 ? topRight : bottomRight) == 1) {
                velocity.setY(0);
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
            if(world.pointCollisionType(velocity.getX() > 0 ? topRight : topLeft) == 1 ||
                    world.pointCollisionType(velocity.getX() > 0 ? bottomRight : bottomLeft) == 1) {
                velocity.setX(0);
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
            changeAnimation(velocity.getX() > 0 ? PlayerAnimations.JUMP_RIGHT : PlayerAnimations.JUMP_LEFT);
            frame = 1;
        }

        if(velocity.getY() < 0 && oldVelocity.getY() >= 0) {
            changeAnimation(velocity.getX() > 0 ? PlayerAnimations.JUMP_RIGHT : PlayerAnimations.JUMP_LEFT);
            frame = 3;
        }

        if(velocity.getY() == 0 && oldVelocity.getY() != 0) {
            if(velocity.getX() == 0) {
                changeAnimation(getAnimation().getId() == PlayerAnimations.JUMP_RIGHT.getId() ? PlayerAnimations.STAND_RIGHT : PlayerAnimations.STAND_LEFT);
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

        return world.pointCollisionType(corner1) == 1 || world.pointCollisionType(corner2) == 1;
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

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

}
