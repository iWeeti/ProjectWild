package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.local.UpdateSpeedMultiplierPacket;

public class SpeedCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(!CommandHandler.isMod(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Admin Only");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(args.length < 1) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Missing Arguments");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        try {
            float speedMultiplier = Float.parseFloat(args[0]);

            UpdateSpeedMultiplierPacket packet = new UpdateSpeedMultiplierPacket(speedMultiplier);
            WildServer.getServer().sendToTCP(client.getSocket(), packet);

            ChatMessagePacket messagePacket = new ChatMessagePacket(String.format("[GREEN]Success![WHITE] Set Speed Multipier To %s", speedMultiplier));
            WildServer.getServer().sendToTCP(client.getSocket(), messagePacket);
        } catch(NumberFormatException e) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Please Enter A Valid Speed Multiplier");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }
    }

    @Override
    public String help() {
        return "Changes Your Speed";
    }

}
