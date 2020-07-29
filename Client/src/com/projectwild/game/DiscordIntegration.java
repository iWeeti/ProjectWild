package com.projectwild.game;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.projectwild.game.ingame.WorldState;
import com.projectwild.game.pregame.WorldSelectionState;
import com.projectwild.shared.packets.world.LeaveWorldPacket;
import com.projectwild.shared.packets.world.RequestWorldPacket;

import javax.swing.*;
import java.nio.charset.Charset;
import java.time.Clock;
import java.util.Base64;
import java.util.Random;

public class DiscordIntegration {

    private static final String applicationId = "737479695729295451";

    private DiscordRPC rpc;

    public DiscordIntegration() {
        rpc = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        handlers.joinRequest = (user) -> {
            int result = JOptionPane.showConfirmDialog(null, String.format("%s#%s is requesting to join you through Discord!", user.username, user.discriminator));
            if (result == JOptionPane.YES_OPTION) {
                rpc.Discord_Respond(user.userId, DiscordRPC.DISCORD_REPLY_YES);
            } else if (result == JOptionPane.NO_OPTION) {
                rpc.Discord_Respond(user.userId, DiscordRPC.DISCORD_REPLY_NO);
            } else {
                rpc.Discord_Respond(user.userId, DiscordRPC.DISCORD_REPLY_IGNORE);
            }
        };

        handlers.joinGame = (worldName) -> {
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

    public void setPresence(String details, String worldName) {
        DiscordRichPresence richPresence = new DiscordRichPresence();
        richPresence.details = details;
        richPresence.startTimestamp = Clock.systemUTC().millis();
        richPresence.largeImageKey = "icon";
        if(worldName != null) {
            // Generating A Random PartyID
            byte[] array = new byte[9];
            new Random().nextBytes(array);
            richPresence.partyId = new String(array, Charset.forName("UTF-8"));

            richPresence.joinSecret = Base64.getEncoder().encodeToString(worldName.getBytes());

            richPresence.partySize = 5;
            richPresence.partyMax = 20;
        }

        rpc.Discord_UpdatePresence(richPresence);
    }

    public DiscordRPC getRPC() {
        return rpc;
    }

}
