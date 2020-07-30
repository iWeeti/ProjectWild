package com.projectwild.server.worlds.players;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.player.UpdatePositionPacket;
import com.projectwild.shared.packets.player.local.UpdateSpeedMultiplierPacket;
import com.projectwild.shared.utils.Vector2;

public class Player {

    private Client client;
    private World world;

    private float speedMultiplier;
    private Vector2 position;
    private boolean override;

    public Player(Client client, World world, Vector2 position) {
        this.client = client;
        this.world = world;
        this.position = position.copy();
        this.override = false;
        speedMultiplier = 1.0f;
    }

    public void updateSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
        UpdateSpeedMultiplierPacket packet = new UpdateSpeedMultiplierPacket(speedMultiplier);
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

    public void checkCollision() {
        // Check For Collision
        Vector2[] corners = new Vector2[] {
                new Vector2(getPosition().getX() + 8, getPosition().getY() + 26),
                new Vector2(getPosition().getX() + 24, getPosition().getY() + 26),
                new Vector2(getPosition().getX() + 8, getPosition().getY()),
                new Vector2(getPosition().getX() + 24, getPosition().getY())
        };

        for(Vector2 v : corners) {
            int blockX = (int) Math.floor(v.getX() / 32.0f);
            int blockY = (int) Math.floor(v.getY() / 32.0f);

            getWorld().getBlocks()[blockY][blockX][1].collision(this);
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

    public String getNametag() {
        String nametag;
        if(client.getUserId() == world.getOwner()) {
            nametag = String.format("[GREEN]%s", client.getUsername());
        } else if(world.hasAccess(client) && world.getOwner() != -1) {
            nametag = String.format("[#98ff73]%s", client.getUsername());
        } else {
            nametag = String.format("[WHITE]%s", client.getUsername());
        }

        if(getClient().getRank() == Rank.DEVELOPER) {
            nametag = String.format("[RED][Dev] %s", nametag);
        } else if(getClient().getRank() == Rank.MOD) {
            nametag = String.format("[YELLOW][Mod] %s", nametag);
        }


        return nametag;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
