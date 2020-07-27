package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIParent;
import com.projectwild.game.gui.components.Background;
import com.projectwild.game.gui.components.Button;
import com.projectwild.game.gui.components.Image;
import com.projectwild.game.gui.components.TextField;
import com.projectwild.shared.packets.LoginDataPacket;
import com.projectwild.shared.utils.Vector2;

public class LoginState implements GameState {

    private GUIParent guiParent;
    private boolean isRegistering;

    private LoginListener loginListener;

    @Override
    public void initialize() {
        loginListener = new LoginListener();
        WildGame.getClient().addListener(loginListener);

        // Creating The GUIParent
        guiParent = new GUIParent();
        Gdx.input.setInputProcessor(guiParent.getInputAdapter());

        // BG & Logo
        guiParent.addComponent(new Background(Color.valueOf("2a2a4d")));
        guiParent.addComponent(new Image(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 275), new Vector2(200), "logo128"));

        createPage1();
    }

    private void createPage1() {
        Button login = new Button(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 400), "Login", Color.valueOf("56569c"));
        Button register = new Button(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 500), "Register", Color.valueOf("56569c"));

        login.setCallback(() -> {
            isRegistering = false;
            guiParent.removeComponent(login);
            guiParent.removeComponent(register);
            createPage2();
        });

        register.setCallback(() -> {
            isRegistering = true;
            guiParent.removeComponent(login);
            guiParent.removeComponent(register);
            createPage2();
        });

        guiParent.addComponent(login);
        guiParent.addComponent(register);
    }

    private void createPage2() {
        TextField username = new TextField(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 400), 200, "Username", Color.valueOf("56569c"), 8);
        guiParent.addComponent(username);

        TextField password = new TextField(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 500), 200, "Password", Color.valueOf("56569c"), 16);
        password.setSecret(true);
        guiParent.addComponent(password);

        Button submit = new Button(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 600), "Submit", Color.valueOf("56569c"));
        submit.setCallback(() -> {
            WildGame.getClient().sendTCP(new LoginDataPacket(username.getText(), password.getText(), isRegistering));
        });
        guiParent.addComponent(submit);

        Button back = new Button(new Vector2(60, Gdx.graphics.getHeight() - 75), "Back", Color.valueOf("56569c"));
        back.setCallback(() -> {
            guiParent.removeComponent(username);
            guiParent.removeComponent(password);
            guiParent.removeComponent(submit);
            guiParent.removeComponent(back);
            createPage1();
        });
        guiParent.addComponent(back);
    }
    
    @Override
    public void update() {

    }
    
    @Override
    public void render() {
        guiParent.render();
    }
    
    @Override
    public void dispose() {
        WildGame.getClient().removeListener(loginListener);
        Gdx.input.setInputProcessor(null);
        guiParent.destroy();
    }
    
}
