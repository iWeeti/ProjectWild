package com.projectwild.game.ingame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectwild.game.WildGame;
import com.projectwild.shared.packets.ChatMessagePacket;

import java.time.Clock;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatHandler {

    private class Message {
        public String content;
        public long time;

        public Message(String message, long time) {
            this.content = message;
            this.time = time;
        }
    }

    private boolean chatOpen;
    private CopyOnWriteArrayList<Message> messages;
    private CopyOnWriteArrayList<String> localMessages;
    private int localCounter;
    private TextInputListener typeListener;

    public ChatHandler() {
        typeListener = new TextInputListener(this);
        messages = new CopyOnWriteArrayList<>();
        localMessages = new CopyOnWriteArrayList<>();
        chatOpen = false;
        localCounter = -1;
    }

    public void render(SpriteBatch sb) {
        if(((WorldState) WildGame.getState()).getInventoryHandler().isInventoryOpen())
            return;

        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_16");
        font.getData().markupEnabled = true;

        GlyphLayout msgLayout = new GlyphLayout(font, (typeListener.getString().startsWith("/") ? "[RED]CMD >> [WHITE]" : "[YELLOW]MSG >> [WHITE]")  + typeListener.getString());

        if (chatOpen)
            font.draw(sb, msgLayout, 50, 50);

        float y = chatOpen ? msgLayout.height + 50 : 50;
        for (Message message : messages) {
            if (chatOpen || message.time + 5000 >  Clock.systemUTC().millis()) {
                GlyphLayout layout = new GlyphLayout(font, message.content);
                y += layout.height;
                font.draw(sb, layout, 50, y);
                y += 15;
            }
        }
    }

    public void addMessage(String message) {
        messages.add(0, new Message(message, Clock.systemUTC().millis()));

        if(messages.size() > 20)
            messages.remove(messages.size()-1);
    }

    public void toggleChat() {
        chatOpen = !chatOpen;
        localCounter = -1;
    }

    public boolean isChatOpen() {
        return chatOpen;
    }

    public TextInputListener getTypeListener() {
        return typeListener;
    }

    public static class TextInputListener extends InputAdapter {

        private ChatHandler chatHandler;
        private String string;

        public TextInputListener(ChatHandler chatHandler) {
            this.chatHandler = chatHandler;
            string = "";
        }

        public void clear() {
            string = "";
        }

        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.ENTER) {
                if(chatHandler.isChatOpen() && !string.isEmpty()) {
                    WildGame.getClient().sendTCP(new ChatMessagePacket(string));

                    chatHandler.localMessages.add(0, string);
                    if(chatHandler.localMessages.size() > 10)
                        chatHandler.localMessages.remove(chatHandler.localMessages.size()-1);
                }
                chatHandler.toggleChat();
                clear();
            }

            if(keycode == Input.Keys.UP) {
                if(chatHandler.isChatOpen()) {
                    if(chatHandler.localMessages.size() - 1 > chatHandler.localCounter)
                        string = chatHandler.localMessages.get(++chatHandler.localCounter);
                }
            }

            if(keycode == Input.Keys.DOWN) {
                if(chatHandler.isChatOpen()) {
                    if(chatHandler.localCounter == 0)
                        string = "";

                    if(0 < chatHandler.localCounter)
                        string = chatHandler.localMessages.get(--chatHandler.localCounter);
                }
            }

            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            if (!chatHandler.isChatOpen()) return true;
            String input = Character.toString(character);
            String regex = "^[a-zA-Z0-9öäåÖÄÅ/!?#$%^&*()_\\-{}|'\\[\\].,\b\\s\":;]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            if(matcher.matches()) {
                if((int) character == 8) {
                    if (!string.isEmpty())
                        string = string.replaceAll(".$", "");
                } else if((int) character != 13) {
                    string += input;
                }
            }
            return true;
        }

        public String getString() {
            return string;
        }
    }

}
