package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.LoginResponsePacket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorldCommand implements Command {
    @Override
    public void execute(Client client, String[] args) {
        StringBuilder worldInfo = new StringBuilder();

        World world = client.getPlayer().getWorld();
        String sql = "SELECT username FROM Users WHERE id = ? COLLATE NOCASE";

        ResultSet rs = WildServer.getDatabaseController().query(sql, world.getOwner());
        try {
            if(rs.isClosed()) {
                WildServer.getServer().sendToTCP(client.getSocket(), new ChatMessagePacket("Something went wrong."));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            worldInfo.append(String.format("%s ownded by %s\n", world.getName(), rs.getString("username")));
        } catch (SQLException throwables) {
            worldInfo.append(world.getName());
            throwables.printStackTrace();
        }
        worldInfo.append(String.format("POS: [%s, %s] %s blocks tall and %s blocks wide\n", Math.floor(client.getPlayer().getPosition().getX()/32), Math.floor(client.getPlayer().getPosition().getY()/32), world.getHeight(), world.getWidth()));

        StringBuilder trusted = new StringBuilder();
        for (int id: world.getTrusted()){
            String _sql = "SELECT username FROM Users WHERE id = ? COLLATE NOCASE";

            ResultSet resultSet = WildServer.getDatabaseController().query(_sql, world.getOwner());
            try {
                if(resultSet.isClosed()) {
                    WildServer.getServer().sendToTCP(client.getSocket(), new ChatMessagePacket("Something went wrong."));
                    return;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                trusted.append(resultSet.getString("username") + ", ");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (!trusted.toString().isBlank() && !trusted.toString().isEmpty())
            worldInfo.append(String.format("Trusted %s", trusted.toString().substring(0, trusted.toString().length() - 2)));

        WildServer.getServer().sendToTCP(client.getSocket(), new ChatMessagePacket(worldInfo.toString()));
    }

    @Override
    public String help() {
        return "Info about the world.";
    }

    @Override
    public Rank rank() {
        return Rank.USER;
    }
}
