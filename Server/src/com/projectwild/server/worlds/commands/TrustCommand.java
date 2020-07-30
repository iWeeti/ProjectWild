package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;

public class TrustCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Player player = (Player) args[0];

        if (player.getClient().getUserId() == world.getOwner()) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]You Cannot Trust Yourself");
            client.sendTCP(packet);
            return;
        }

        if(world.hasAccess(player.getClient())) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]This Player Is Already Trusted");
            client.sendTCP(packet);
            return;
        }

        // Adding Trusted And Sending messages
        world.addTrusted(player.getClient());

        client.sendChatMessage(String.format("[GREEN]Success! [WHITE]Trusted %s", player.getClient().getUsername()));
        player.getClient().sendChatMessage(String.format("[GREEN]Trusted! [WHITE]You Are Now Trusted In %s", world.getName()));

        // Setting Local Player Access
        player.getClient().sendTCP(new UpdateHasAccessPacket(true));

        // Updating Nametag
        UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(player.getClient().getUserId(), player.getNametag());
        for(Player ply : world.getPlayers()) {
            ply.getClient().sendTCP(nameTagPacket);
        }
    }

    @Override
    public String help() {
        return "Allows You To Trust Players";
    }

    @Override
    public boolean worldOwnerOnly() {
        return true;
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.PLAYER};
    }

}
