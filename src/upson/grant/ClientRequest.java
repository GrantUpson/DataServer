package upson.grant;

import state.*;
import java.io.*;
import java.net.Socket;

public class ClientRequest implements Runnable
{
    private final Socket clientConnection;
    private State currentState;

    public ClientRequest(Socket connection)
    {
        this.clientConnection = connection;
        this.currentState = new Connected(this);
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
                String result = currentState.parseCommand(command);
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

    public void changeState(State newState)
    {
        this.currentState = newState;
    }
}
