package se.c0la.narwhalirc;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

public class Network implements Runnable 
{
    private static class PostLog implements Runnable
    {
        private MainFrame frame;
        private String message;
        
        public PostLog(MainFrame frame, String message)
        {
            this.frame = frame;
            this.message = message;
        }
        
        @Override
        public void run()
        {
            frame.log(message);
        }
    }
    
    private MainFrame frame;
    private Socket socket;
    private Executor exec;
    private PrintWriter writer;
    
    public Network(MainFrame frame)
    {
        socket = new Socket();
        exec = Executors.newSingleThreadExecutor();
        this.frame = frame;
    }
    
    public void connect(String host, int port)
    throws IOException
    {
        socket.connect(new InetSocketAddress(host, port));
        writer = new PrintWriter(socket.getOutputStream());
        writer.println("NICK narwhalirc");
        writer.println("USER foo foo foo foo");
        writer.println("JOIN #floodffs!");
        writer.flush();
        exec.execute(this);
    }
    
    public void sendMessage(String message)
    {
        writer.println(message);
        writer.flush();
    }
    
    @Override
    public void run()
    {
        try {
            InputStream stream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            
            String line = null;
            while ((line = reader.readLine()) != null) {
                processMessage(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void processMessage(String message)
    {
        String[] comp = MessageTokenizer.tokenize(message);
        if ("PRIVMSG".equalsIgnoreCase(comp[1])) {
            String nick = MessageTokenizer.getNick(comp[0]);
            String log = String.format("<%s> %s", nick, comp[3]);
            SwingUtilities.invokeLater(new PostLog(frame, log));
        }
        // :fatcat.c0la.se PING narwhalirc
        else if ("PING".equalsIgnoreCase(comp[1])) {
            sendMessage("PONG :" + comp[2]);
        }
        else {
            SwingUtilities.invokeLater(new PostLog(frame, message));
        }
    }
}