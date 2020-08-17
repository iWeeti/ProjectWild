package com.projectwild.game;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.packets.world.LeaveWorldPacket;
import com.projectwild.shared.packets.world.RequestWorldPacket;

import java.nio.charset.Charset;
import java.time.Clock;
import java.util.Base64;
import java.util.Random;

public class DiscordIntegration {

    private static final String applicationId = "737479695729295451";

    private DiscordRPC rpc;
    private DiscordRichPresence richPresence;

    public DiscordIntegration() {
        rpc = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        handlers.joinGame = (worldName) -> {
            if(!(WildGame.getState() instanceof WorldState) && !(WildGame.getState() instanceof WorldSelectionState))
                return;

            StringBuilder worldNameBuilder = new StringBuilder();
            byte[] worldNameBytes = Base64.getDecoder().decode(worldName);
            for (byte b : worldNameBytes)
                worldNameBuilder.append((char) b);
            worldName = worldNameBuilder.toString();

            // Leaving Current World If Needed
            if(WildGame.getState() instanceof WorldState) {
                LeaveWorldPacket leaveWorldPacket = new LeaveWorldPacket();
                WildGame.getClient().sendTCP(leaveWorldPacket);
                WildGame.changeState(new WorldSelectionState());
            }

            // Requesting World
            WildGame.getClient().sendTCP(new RequestWorldPacket(worldName));
        };

        // Initialize
        rpc.Discord_Initialize(applicationId, handlers, true, "");

        // Setup Callbacks
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void setPresence(String state) {
        richPresence = new DiscordRichPresence();
        richPresence.state = state;
        richPresence.startTimestamp = Clock.systemUTC().millis();
        richPresence.largeImageKey = "icon";

        rpc.Discord_UpdatePresence(richPresence);
    }

    public void setPresence(String state, String world) {
        richPresence = new DiscordRichPresence();
        richPresence.state = state;
        richPresence.startTimestamp = Clock.systemUTC().millis();
        richPresence.largeImageKey = "icon";

        // Generating A Random PartyID
        byte[] array = new byte[9];
        new Random().nextBytes(array);
        richPresence.partyId = new String(array, Charset.forName("UTF-8"));

        richPresence.partySize = 1;
        richPresence.partyMax = 100;

        richPresence.joinSecret = Base64.getEncoder().encodeToString(world.getBytes());

        rpc.Discord_UpdatePresence(richPresence);
    }

    public void setPresenceWorldPlayers(int players) {
        richPresence.partySize = players;
        rpc.Discord_UpdatePresence(richPresence);
    }

    public DiscordRPC getRPC() {
        return rpc;
    }

}
