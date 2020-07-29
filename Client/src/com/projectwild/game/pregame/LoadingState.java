package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;

public class LoadingState implements GameState {

    private Texture kodama;
    private SpriteBatch sb;
    private long start;

    @Override
    public void initialize() {
        sb = new SpriteBatch();
        start = System.currentTimeMillis();
        kodama = WildGame.getAssetManager().getAsset("kodama");
        WildGame.getDiscordIntegration().setPresence("Loading...", null);
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() >= start + 3400)
            WildGame.changeState(new LoginState());
    }

    @Override
    public void render() {
        sb.begin();
            Color color = Color.valueOf("2a2a4d");
            if(System.currentTimeMillis() - start < 3000)
                color.a = (System.currentTimeMillis() - start) / 3000f;
            sb.setColor(color);
            sb.draw(kodama, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {

    }

}
