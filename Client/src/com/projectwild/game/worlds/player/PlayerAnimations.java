package com.projectwild.game.worlds.player;

public enum PlayerAnimations {

    WALK_RIGHT(0, 8, 12f),
    WALK_LEFT(1, 8, 12f),
    STAND_RIGHT(2, 6, 5f),
    STAND_LEFT(3, 6, 5f),
    JUMP_RIGHT(4, 4, 0f),
    JUMP_LEFT(5, 4, 0f);

    private int id;
    private int length;
    private float speed;

    PlayerAnimations(int id, int length, float speed) {
        this.id = id;
        this.length = length;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public float getSpeed() {
        return speed;
    }

    public static PlayerAnimations getAnimation(int id) {
        for(PlayerAnimations animations : PlayerAnimations.values()) {
            if(animations.getId() == id)
                return animations;
        }
        return STAND_RIGHT;
    }

}
