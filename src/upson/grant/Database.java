package upson.grant;

import java.sql.*;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Database
{
    private final String url = "jdbc:mysql://localhost:3306/data_server?serverTimezone=Australia/Melbourne";

    private final String username;
    private final String password;

    public Database(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password)
    {
        int numberOfRows = 0;

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Select * from account where username = ? and password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                numberOfRows++;
            }
        }
        catch (SQLException sqlException)
        {
            System.out.println("Error " + sqlException.getMessage());
        }

        return (numberOfRows > 0);
    }

    public boolean usernameExists(String username)
    {
        int numberOfRows = 0;

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Select * from account where username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                numberOfRows++;
            }
        }
        catch (SQLException sqlException)
        {
            System.out.println("Error " + sqlException.getMessage());
        }

        return (numberOfRows > 0);
    }

    public boolean register(String username, String password)
    {
        boolean successful = false;

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
            successful = true;
        }
        catch (SQLException sqlException)
        {
            System.out.println("Error " + sqlException.getMessage());
        }

        return successful;
    }
}
