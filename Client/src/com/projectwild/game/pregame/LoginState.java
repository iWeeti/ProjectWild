package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.projectwild.game.GameState;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.pregame.GUIParent;
import com.projectwild.game.gui.pregame.components.*;
import com.projectwild.shared.packets.LoginDataPacket;
import com.projectwild.shared.utils.Vector2;

public class LoginState implements GameState {

    private GUIParent guiParent;
    private boolean isRegistering;

    private LoginListener loginListener;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void initialize() {
        // Playing Music
        WildGame.getAssetManager().getSound("menu").loop(0.35f);
        
        // Creating The GUIParent
        guiParent = new GUIParent();
        Gdx.input.setInputProcessor(guiParent.getInputAdapter());

        // Setting Up The State
        loginListener = new LoginListener(guiParent);
        WildGame.getClient().addListener(loginListener);

        WildGame.getDiscordIntegration().setPresence("In Main Menu");

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
        String _username = "";
        String _password = "";

        // get file, has to be external cause can't write to an internal file.
        FileHandle file = Gdx.files.external("KodamaStudios\\ProjectWild\\login.txt");
        if(!isRegistering) {
            if (file.exists()) {
                String[] data = file.readString().split("\n");
                // has 2 entries
                if (data.length == 2) {
                    _username = data[0];
                    _password = data[1];
                }
            }
        }

        TextField username = new TextField(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 400), 200, "Username", Color.valueOf("56569c"), 12, _username);
        guiParent.addComponent(username);

        TextField password = new TextField(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 500), 200, "Password", Color.valueOf("56569c"), 16, _password);
        password.setSecret(true);
        guiParent.addComponent(password);

        Button submit = new Button(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 600), "Submit", Color.valueOf("56569c"));
        submit.setCallback(() -> {
            //write the credentials on every login or register
            file.writeString(String.format("%s\n%s", username.getText(), password.getText()), false);
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

    public void addNotification(String text) {
        guiParent.addComponent(new Notification(3500, text, Color.valueOf("56569c")));
    }
    
}
