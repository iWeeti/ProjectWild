package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;

public class ForceUntrustCommand implements Command{

    @Override
    public void execute(Client client, String[] args) {
        World world = client.getPlayer().getWorld();


        if(args.length < 1) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Missing Arguments");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        Client c = WildServer.getClientHandler().getClientByUsername(args[0]);
        if(c == null) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]Couldn't Find Player");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        boolean isTrusted = false;
        for(int i : world.getTrusted()) {
            if(c.getUserId() == i)
                isTrusted = true;
        }

        if(!isTrusted) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]This Player Is Not Trusted");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        // Untrusting & Removing Access
        world.removeTrusted(c);
        ChatMessagePacket packet = new ChatMessagePacket(String.format("[GREEN]Success! [WHITE]Untrusted %s", c.getUsername()));
        WildServer.getServer().sendToTCP(client.getSocket(), packet);

        packet = new ChatMessagePacket(String.format("[RED]Untrusted! [WHITE]You Are No Longer Trusted In %s", world.getName()));
        WildServer.getServer().sendToTCP(c.getSocket(), packet);

        // Updating Local Clients Access
        UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(false);
        WildServer.getServer().sendToTCP(c.getSocket(), hasAccessPacket);

        // Updating Nametag
        UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : world.getPlayers()) {
            WildServer.getServer().sendToTCP(ply.getClient().getSocket(), nameTagPacket);
        }
    }

    @Override
    public String help() {
        return "Allows You To Remove Trusted Players";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

}
