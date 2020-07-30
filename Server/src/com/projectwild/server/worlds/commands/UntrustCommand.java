package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.player.UpdateNameTagPacket;
import com.projectwild.shared.packets.player.local.UpdateHasAccessPacket;

public class UntrustCommand implements Command{

    @Override
    public void execute(Client client, World world, Object[] args) {
        Client c = (Client) args[0];

        boolean isTrusted = false;
        for(int i : world.getTrusted()) {
            if(c.getUserId() == i)
                isTrusted = true;
        }

        if(!isTrusted) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed! [WHITE]This Player Is Not Trusted");
            client.sendTCP(packet);
            return;
        }

        // Untrusting & Removing Access
        world.removeTrusted(c);

        client.sendChatMessage(String.format("[GREEN]Success! [WHITE]Untrusted %s", c.getUsername()));
        c.sendChatMessage(String.format("[RED]Untrusted! [WHITE]You Are No Longer Trusted In %s", world.getName()));

        // Updating Local Clients Access
        UpdateHasAccessPacket hasAccessPacket = new UpdateHasAccessPacket(false);
        c.sendTCP(hasAccessPacket);

        // Updating Nametag
        UpdateNameTagPacket nameTagPacket = new UpdateNameTagPacket(c.getUserId(), c.getPlayer().getNametag());
        for(Player ply : world.getPlayers()) {
            ply.getClient().sendTCP(nameTagPacket);
        }
    }

    @Override
    public String help() {
        return "Allows You To Remove Trusted Players";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.CLIENT};
    }

}
