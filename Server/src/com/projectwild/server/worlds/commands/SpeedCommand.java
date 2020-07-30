package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.local.UpdateSpeedMultiplierPacket;

public class SpeedCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(args.length < 1) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Missing Arguments");
            client.sendTCP(packet);
            return;
        }

        try {
            float speedMultiplier = Float.parseFloat(args[0]);

            UpdateSpeedMultiplierPacket packet = new UpdateSpeedMultiplierPacket(speedMultiplier);
            client.sendTCP(packet);

            ChatMessagePacket messagePacket = new ChatMessagePacket(String.format("[GREEN]Success![WHITE] Set Speed Multipier To %s", speedMultiplier));
            client.sendTCP(messagePacket);
        } catch(NumberFormatException e) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Please Enter A Valid Speed Multiplier");
            client.sendTCP(packet);
            return;
        }
    }

    @Override
    public String help() {
        return "Changes Your Speed";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

}
