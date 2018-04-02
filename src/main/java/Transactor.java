import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.*;

public class Transactor {
    DBUtil dbObject;

    public Transactor(){
        try {
            dbObject = new DBUtil();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public JSONObject addTransaction(String[] args) throws ParseException, SQLException, IOException {
            return dbObject.addTransaction(args[2]);
    }

    public JSONObject aggTransaction(String[] args) throws SQLException {

        if (args[1].equals("sum")) {
            System.out.println("Sum function!!");
            return dbObject.getSum(args[0]);
        }

        else if (args[1].equals("list")){
            System.out.println("List function");
            return dbObject.getListOfTransactions(args[0]);
        }

        else {
            System.out.println("All Transactions Method");
            return dbObject.getAllTransaction(args[0],args[1]);
        }
    }


    public JSONObject getResults(String[] args) throws SQLException, ParseException, IOException {

        if (args.length==3){
            return addTransaction(args);
        }
        else if (args.length==2){
            return aggTransaction(args);
        }
        return null;

    }

}
