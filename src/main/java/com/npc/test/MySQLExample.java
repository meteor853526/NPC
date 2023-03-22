package com.npc.test;

import java.sql.*;

public class MySQLExample {


        private String message = null;
        private int id = 0;


    public MySQLExample(String message, int id) throws SQLException {
        this.message = message;
        this.id = id;
    }
    public MySQLExample() throws SQLException {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public void IntoDB(int msgid,String message) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://34.80.170.197/Npc";
        String username = "user3";
        String password = "123456789";
        Connection conn = DriverManager.getConnection(url, username, password);
        String sql = "SELECT * FROM infor";
        String insert = "INSERT INTO infor(id,message)"
                +"values(?,?)";

        PreparedStatement ptmt = conn.prepareStatement(insert);

        ptmt.setInt(1,msgid);
        ptmt.setString(2,message);



        ptmt.executeUpdate();

    }



}