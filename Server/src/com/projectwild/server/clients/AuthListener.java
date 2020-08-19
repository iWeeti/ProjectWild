package com.projectwild.server.clients;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.server.WildServer;
import com.projectwild.shared.packets.ChangePasswordResponsePacket;
import com.projectwild.shared.packets.LoginDataPacket;
import com.projectwild.shared.packets.LoginResponsePacket;
import com.projectwild.shared.packets.RequestPasswordChangePacket;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthListener extends Listener {
    
    @Override
    public void connected(Connection connection) {
    
    }
    
    @Override
    public void disconnected(Connection connection) {
        Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
        if(client == null)
            return;
    
        WildServer.getClientHandler().disconnectClient(client);

        if(client.getPlayer() == null)
            return;

        if(client.getPlayer().getWorld() == null)
            return;

        client.getPlayer().getWorld().destroyPlayer(client.getPlayer());
    }
    
    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof LoginDataPacket) {
            LoginDataPacket packet = (LoginDataPacket) obj;
            String username = packet.getUsername();
            String password = packet.getPassword();
            
            if(WildServer.getClientHandler().getClientBySocket(connection.getID()) != null) {
                connection.sendTCP(new LoginResponsePacket(false, "Uhhh... You can not login twice."));
                return;
            }
        
            if(username == null || password == null) {
                connection.sendTCP(new LoginResponsePacket(false, "Please enter a username and a password please."));
                return;
            }
        
            if(username.length() > 12 || username.length() < 3 || password.length() > 16 || password.length() < 3) {
                connection.sendTCP(new LoginResponsePacket(false, "Zzz. Something is way too long or too short."));
                return;
            }
        
            //Make Username Only Alphanumeric Characters
            String regex = "^[a-zA-Z0-9]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(username);
            if(!matcher.matches()) {
                connection.sendTCP(new LoginResponsePacket(false, "Sorry. We only allow letters and numbers in usernames. None of that fancy stuff."));
                return;
            }
        
            try {
                if(packet.isRegistering()) {
                    // Tries to register client
                    if(getAccount(username) != null) {
                        connection.sendTCP(new LoginResponsePacket(false, "Ooops. Looks like an account by that name already exists."));
                        return;
                    }

                    String sql = "INSERT INTO Users(username,password) VALUES(?,?)";
                    WildServer.getDatabaseController().insert(sql, username, new StrongPasswordEncryptor().encryptPassword(password));
                
                    connection.sendTCP(new LoginResponsePacket(true, String.format("Successfully registered! Welcome to Project Wild %s.", username)));
                
                    ResultSet rs = getAccount(username);
                    if(rs == null) {
                        connection.sendTCP(new LoginResponsePacket(false, "Uh oh. Something went wrong. Try registering again."));
                        return;
                    }
                    WildServer.getClientHandler().loginClient(new Client(connection.getID(), rs.getInt("id")));
                } else {
                    // Tries to login client
                    String sql = "SELECT id, password FROM Users WHERE username = ? COLLATE NOCASE";

                    ResultSet rs = WildServer.getDatabaseController().query(sql, username);
                    if(rs.isClosed()) {
                        connection.sendTCP(new LoginResponsePacket(false, "Huh. An account by that name does not seem to exist."));
                        return;
                    }
                
                    if(WildServer.getClientHandler().getClientByUserId(rs.getInt("id")) != null) {
                        connection.sendTCP(new LoginResponsePacket(false, "Sorry, looks like that account is already logged in."));
                        return;
                    }
                
                    if(!new StrongPasswordEncryptor().checkPassword(password, rs.getString("password"))) {
                        connection.sendTCP(new LoginResponsePacket(false, "Wrong password! Try again."));
                    } else {
                        connection.sendTCP(new LoginResponsePacket(true, "Successfully logged in!"));
                        WildServer.getClientHandler().loginClient(new Client(connection.getID(), rs.getInt("id")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                connection.sendTCP(new LoginResponsePacket(false, "Unknown Error. Try again."));
            }
        }
        
        if(obj instanceof RequestPasswordChangePacket) {
            RequestPasswordChangePacket packet = (RequestPasswordChangePacket) obj;
    
            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
            if(client == null)
                return;
    
            String sql = "SELECT password FROM Users WHERE id = ?";
    
            ResultSet rs = WildServer.getDatabaseController().query(sql, client.getUserId());
            try {
                if(rs.isClosed()) {
                    WildServer.getClientHandler().disconnectClient(client);
                    return;
                }
    
                if(!new StrongPasswordEncryptor().checkPassword(packet.getCurrentPassword(), rs.getString("password"))) {
                    connection.sendTCP(new ChangePasswordResponsePacket(false, "Wrong Current Password. Try Again."));
                } else {
                    if(packet.getPassword().length() > 16 || packet.getPassword().length() < 3) {
                        connection.sendTCP(new ChangePasswordResponsePacket(false, "Password Needs To Be Within 3-16 Characters Long."));
                        return;
                    }
                    
                    sql = "UPDATE Users SET password = ? WHERE id = ?";
                    WildServer.getDatabaseController().update(sql, new StrongPasswordEncryptor().encryptPassword(packet.getPassword()), client.getUserId());
                    connection.sendTCP(new ChangePasswordResponsePacket(true, "Successfully Changed Your Password."));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
        }
        
    }

    private ResultSet getAccount(String username) {
        String sql = "SELECT id FROM Users WHERE username = ? COLLATE NOCASE";

        ResultSet rs = WildServer.getDatabaseController().query(sql, username);
        try {
            if(rs.next())
                return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}