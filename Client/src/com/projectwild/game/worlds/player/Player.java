package com.projectwild.game.worlds.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectwild.game.WildGame;
import com.projectwild.shared.utils.Vector2;

public class Player {

    private int userId;
    private String username;
    private Vector2 position;

    private PlayerAnimations animation;
    protected float frame;

    public Player(int userId, String username) {
        this.userId = userId;
        this.username = username;
        position = new Vector2();
        changeAnimation(PlayerAnimations.STAND_RIGHT.getId());
    }

    public void changeAnimation(int animationId) {
        animation = PlayerAnimations.getAnimation(animationId);
        frame = 0;
    }

    public void render(SpriteBatch sb) {
        TextureRegion[][] texture = TextureRegion.split(WildGame.getAssetManager().getAsset("player"), 32, 32);
        sb.draw(texture[animation.getId()][(int) Math.floor(frame)], position.getXInt(), position.getYInt());

        if(animation.getSpeed() != 0)
            frame += animation.getSpeed() / 60.0f;

        if(frame >= animation.getLength())
            frame = 0;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Vector2 getPosition() {
        return position;
    }

    public PlayerAnimations getAnimation() {
        return animation;
    }

}
