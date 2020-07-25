package com.projectwild.server.worlds.players;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.player.UpdatePositionPacket;
import com.projectwild.shared.packets.player.local.UpdatePlayerAttributesPacket;
import com.projectwild.shared.utils.Vector2;

public class Player {

    private Client client;
    private World world;

    private float speedMultiplier;
    private Vector2 position;

    public Player(Client client, World world, Vector2 position) {
        this.client = client;
        this.world = world;
        this.position = position.copy();
        speedMultiplier = 1.0f;
    }

    public void updateSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
        UpdatePlayerAttributesPacket packet = new UpdatePlayerAttributesPacket(speedMultiplier);
        WildServer.getServer().sendToTCP(client.getSocket(), packet);
    }

    public void updatePosition(Vector2 position, boolean force) {
        this.position.set(position);
        UpdatePositionPacket packet = new UpdatePositionPacket(client.getUserId(), position);
        for(Player ply : world.getPlayers()) {
            if(force || (ply.getClient().getUserId() != this.getClient().getUserId()))
                WildServer.getServer().sendToTCP(ply.getClient().getSocket(), packet);
        }
    }

    public Client getClient() {
        return client;
    }

    public World getWorld() {
        return world;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public Vector2 getPosition() {
        return position;
    }

}
