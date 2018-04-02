import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class App {
    public static  void main(String[] args) throws SQLException, ParseException, IOException {
        Transactor transact = new Transactor();
        JSONObject result = transact.getResults(args);
        System.out.println(result.toJSONString());
    }
}
