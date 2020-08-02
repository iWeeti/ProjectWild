package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.clothing.UpdateEquippedPacket;
import com.projectwild.shared.packets.player.PlayerRemovePacket;
import com.projectwild.shared.packets.player.PlayerSpawnPacket;

public class InvisibleCommand implements Command {
    @Override
    public void execute(Client client, World world, Object[] args) {
        client.setInvisible(!client.isInvisible());
        if (client.isInvisible()){
            client.sendChatMessage("[GREEN]Success! [WHITE]You're now [GREEN]invisible[WHITE].");
            PlayerRemovePacket playerRemovePacket = new PlayerRemovePacket(client.getUserId());
            for (Player player : world.getPlayers()) {
                if (player.getClient().getUserId() != client.getUserId()){
                    player.getClient().sendChatMessage("[YELLOW]%s [WHITE]has left the world.", player.getNametag());
                    player.getClient().sendTCP(playerRemovePacket);
                }
            }
        } else {
            client.sendChatMessage("[GREEN]Success! [WHITE]You're now [RED]visible[WHITE].");
            UpdateEquippedPacket equippedPacket = new UpdateEquippedPacket(client.getUserId(), client.getEquipped());
            for (Player player : world.getPlayers()){
                if (player.getClient().getUserId() != client.getUserId()){
                    player.getClient().sendChatMessage("[YELLOW]%s [WHITE]has joined the world.", player.getNametag());
                    player.getClient().sendTCP(new PlayerSpawnPacket(client.getUserId(), client.getPlayer().getNametag(), client.getPlayer().getPosition(), false));
                    player.getClient().sendTCP(equippedPacket);
                }
            }
        }
    }

    @Override
    public String help() {
        return "Toggle invisibility.";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[0];
    }
}
