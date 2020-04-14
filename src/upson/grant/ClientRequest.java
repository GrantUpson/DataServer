package upson.grant;

import java.io.*;
import java.net.Socket;

public class ClientRequest implements Runnable
{
    private enum State { LOGGED_OUT, LOGGED_IN };
    private final Socket clientConnection;

    private final String CONNECTION_MENU = "[Register]: Register an account. /[Login]: Log in to your account.";
    private final String MENU = "";

    private State currentState;

    public ClientRequest(Socket connection)
    {
        this.clientConnection = connection;
        this.currentState = State.LOGGED_OUT;
    }

    @Override
    public void run()
    {
        try(BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientConnection.getOutputStream()));
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream())))
        {
            clientWriter.write(CONNECTION_MENU + "\r\n");
            clientWriter.flush();

            String command = clientReader.readLine().toLowerCase();

            while(!command.equalsIgnoreCase("exit"))
            {
                String result = parseCommand(command);
                clientWriter.write(result + "\r\n");
                clientWriter.flush();
                command = clientReader.readLine().toLowerCase();
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getLocalizedMessage());
        }
    }

    public String parseCommand(String command)
    {

        return "";
    }
}
