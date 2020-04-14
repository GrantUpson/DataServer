package upson.grant;

/*
  @author Grant Upson : 385831
*/

import com.mysql.cj.protocol.Resultset;

import javax.xml.transform.Result;
import java.sql.*;

public class Database
{
    public int databaseConnection(String username,String password)
    {
        Connection conn = null;
        int numberRow = 0;
        String url = "jdbc:mysql://localhost:3306/data_server";
        String user = "root";
        String databasePassword = "";
        PreparedStatement preparedStatement = null;
        try
        {
            // db parameters
            // create a connection to the database

            conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();

            String login = "Select * from account where username = ? and password = ?";
            preparedStatement = conn.prepareStatement(login);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet result = preparedStatement.executeQuery();

            while(result.next())
                {
                    numberRow = result.getInt("count(*)");
                }

            conn.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return numberRow;
    }
}
