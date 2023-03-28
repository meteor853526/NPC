package com.npc.test;

//import org.json.JSONArray;
//import org.json.JSONObject;



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


    public String getData() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://34.80.170.197/npc";
        String username = "user3";
        String password = "123456789";
        Connection conn = DriverManager.getConnection(url, username, password);
        String sql = "SELECT * FROM npc WHERE id=1";
        Statement getData = conn.createStatement();
        ResultSet rs = getData.executeQuery(sql);
        String data = null;
        {
            String name = null;
            String role = null;
            String personality = null;
            String favorability = null;
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                name = rs.getString("name");
                role = rs.getString("role");
                personality = rs.getString("personality");
                favorability = rs.getString("favorability");
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", name: " + name);
                System.out.print(", role: " + role);
                System.out.println(", personality: " + personality);
                System.out.println(", favorability: " + favorability);

                data = "{ID:" + id + ", name:" + name + ",role:" + role + ",personality:" + personality + ", favorability:" + favorability + " }";
            }
        }
        return data;
    }


    public void IntoDB(int msgid,String message) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://34.80.170.197/npc";
        String username = "user3";
        String password = "123456789";
        Connection conn = DriverManager.getConnection(url, username, password);
        String sql = "SELECT * FROM npc WHERE id=1";
        String insert = "INSERT INTO infor(id,message)"
                +"values(?,?)";

        PreparedStatement ptmt = conn.prepareStatement(insert);
        Statement getData = conn.createStatement();

        ptmt.setInt(1,msgid);
        ptmt.setString(2,message);



        ptmt.executeUpdate();
        ResultSet rs = getData.executeQuery(sql);
        {

            String name = null;
            String role = null;
            String personality = null;
            String favorability = null;
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                name = rs.getString("name");
                role = rs.getString("role");
                personality = rs.getString("personality");
                favorability = rs.getString("favorability");
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", name: " + name);
                System.out.print(", role: " + role);
                System.out.println(", personality: " + personality);
                System.out.println(", favorability: " + favorability);

                PlayerChatEvent.NpcData = "{ID:"+id+", name:"+name+",role:"+role+",personality:"+personality +", favorability:"+favorability+" }";
            }
        }

    }
//    public static JSONArray convert(ResultSet resultSet) throws Exception {
//
//        JSONArray jsonArray = new JSONArray();
//
//        while (resultSet.next()) {
//
//            int columns = resultSet.getMetaData().getColumnCount();
//            JSONObject obj = new JSONObject();
//
//            for (int i = 0; i < columns; i++)
//                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
//
//            jsonArray.put(obj);
//        }
//        return jsonArray;
//    }


}