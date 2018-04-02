import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DBUtil {
    static final String JDBC_Driver = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/transaction?useSSL=false";
    static final String USER = "root";
    static final String PASS = "";


    Connection conn = null;
    Statement stmt = null;
    String sql;

    DBUtil() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
    }

    public JSONObject getSum(String userId) throws SQLException {
        sql = "SELECT user_id,sum(amount) as amount from transaction where user_id = "+userId;
        ResultSet rs = stmt.executeQuery(sql);
        JSONObject resultObject = new JSONObject();

        while(rs.next()){
            resultObject.put("user_id",rs.getInt("user_id"));
            resultObject.put("sum",rs.getInt("amount"));

        }
        rs.close();
        stmt.close();
        return resultObject;
    }

    public JSONObject getListOfTransactions(String userId) throws SQLException{
        sql = "SELECT * from transaction where user_id = "+userId;
        ResultSet rs = stmt.executeQuery(sql);
        JSONObject resultObject = new JSONObject();

        while(rs.next()){
            resultObject.put("transaction_id",rs.getString(1));
            resultObject.put("amount",rs.getFloat(2));
            resultObject.put("description",rs.getString(3));
            resultObject.put("user_id",rs.getString(4));
            resultObject.put("Date",rs.getDate(5));
        }
        rs.close();
        stmt.close();
        return resultObject;

    }

    public JSONObject getAllTransaction(String user_id,String transactionID) throws SQLException {
        transactionID = "\"" +transactionID + "\"";
        sql = "SELECT * FROM transaction WHERE transaction_id = "+ transactionID+ "and + user_id = "+ user_id;

        ResultSet rs = stmt.executeQuery(sql);
        JSONObject resultObject = new JSONObject();

        if (!rs.isBeforeFirst() ) resultObject.put("Transactions not found", null);

        while(rs.next()){
            resultObject.put("transaction_id",rs.getString(1));
            resultObject.put("amount",rs.getFloat(2));
            resultObject.put("description",rs.getString(3));
            resultObject.put("user_id",rs.getString(4));
            resultObject.put("Date",rs.getDate(5));
        }
        rs.close();
        stmt.close();
        return resultObject;

    }

    public JSONObject addTransaction(String JSONPath) throws IOException, ParseException, SQLException {
        JSONParser jsonParser = new JSONParser();
        FileReader f=new FileReader(JSONPath); 
        Object json_blob = jsonParser.parse(f);
        JSONObject ob  = (JSONObject) json_blob;
        double amount = (Double) ob.get("amount");
        System.out.println(amount);
        String description = (String) ob.get("description");
        String description2 = "\""+description+"\"";
        String date = (String) ob.get("date");
        date = "'"+date+"'";
        long userId =  (Long) ob.get("user_id");
        sql = "INSERT INTO transaction " + String.format("VALUES (LEFT(UUID(),8),%f,%s,%d,%s)",
        		amount,description2,userId,date);
        System.out.println(sql);
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.executeUpdate();
//        ResultSet resultSet = stmt.executeQuery(sql);
        JSONObject res = new JSONObject();
        res.put("Transaction_Id" ,null);
        res.put("User_id ",userId );
        res.put("amount", amount);
        res.put("description", description);
        res.put("Date", date);
        return res;
    }
}
