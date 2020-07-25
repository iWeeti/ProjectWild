package com.projectwild.server;

import java.sql.*;

public class DatabaseController {
    
    private Connection db;
    
    public DatabaseController(String path) {
        db = null;
        try {
            String url = "jdbc:sqlite:"+path;
            db = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public ResultSet query(String sql, Object... vars) {
        PreparedStatement pstmt;
        try {
            pstmt = db.prepareStatement(sql);
            for(int i = 0; i < vars.length; i++) {
                pstmt.setObject(i+1, vars[i]);
            }
            
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void insert(String sql, Object... vars) {
        execute(sql, vars);
    }
    
    public void delete(String sql, Object... vars) {
        execute(sql, vars);
    }
    
    public void update(String sql, Object... vars) {
        execute(sql, vars);
    }
    
    private void execute(String sql, Object[] vars){
        PreparedStatement pstmt;
        try {
            pstmt = db.prepareStatement(sql);
            for(int i = 0; i < vars.length; i++) {
                pstmt.setObject(i+1, vars[i]);
            }
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return db;
    }
}
