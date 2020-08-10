package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;

public class LoadingState implements GameState {

    private Texture kodama;
    private SpriteBatch sb;
    private ShapeRenderer sr;
    private long start;

    @Override
    public void initialize() {
        WildGame.getAssetManager().getSound("whoosh").play();
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        start = System.currentTimeMillis();
        kodama = WildGame.getAssetManager().getAsset("kodama");
        WildGame.getDiscordIntegration().setPresence("Loading...");

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER)
                    WildGame.changeState(new LoginState());

                return false;
            }
        });
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() >= start + 3400)
            WildGame.changeState(new LoginState());
    }

    @Override
    public void render() {
        Color color = Color.valueOf("2a2a4d");
        if(System.currentTimeMillis() - start < 3000)
            color.a = (System.currentTimeMillis() - start) / 3000f;

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(color);
            sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sr.end();

        sb.begin();
            sb.setColor(color);
            sb.draw(kodama, Gdx.graphics.getWidth() / 2f - Gdx.graphics.getHeight() * 1.56f / 2, 0, Gdx.graphics.getHeight() * 1.56f, Gdx.graphics.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

}
