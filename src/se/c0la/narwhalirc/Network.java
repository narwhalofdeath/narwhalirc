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
    
    private String nick;
    private String realname;
    private String channel;
    
    public Network(MainFrame frame)
    {
        socket = new Socket();
        exec = Executors.newSingleThreadExecutor();
        this.frame = frame;
    }
    
    public void setNick(String nick) { this.nick = nick; }
    public String getNick() { return nick; }
    
    public void setRealName(String realname) { this.realname = realname; }
    public String getRealName() { return realname; }
    
    public void setChannel(String channel) { this.channel = channel; }
    public String getChannel() { return channel; }
    
    public void connect(String host, int port)
    throws IOException
    {
        socket.connect(new InetSocketAddress(host, port));
        
        System.out.println("connected");
        
        writer = new PrintWriter(socket.getOutputStream());
        writer.println("NICK " + nick);
        writer.println(String.format("USER %s %s foo :%s", nick, nick, realname));
        writer.flush();
        
        System.out.println("sent ident");
        
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
            String[] msgSplit = comp[3].split(" ");
            if (msgSplit[0].startsWith(String.format("%cVERSION", 1))) {
                sendMessage(String.format("NOTICE %s :%cVERSION narwhalirc%c", nick, 1, 1));
                SwingUtilities.invokeLater(new PostLog(frame, nick + " was curious about your version."));
            }
            else {
                String log = String.format("<%s> %s", nick, comp[3]);
                SwingUtilities.invokeLater(new PostLog(frame, log));
            }
        }
        else if ("001".equals(comp[1])) {
            sendMessage("JOIN " + channel);
            SwingUtilities.invokeLater(new PostLog(frame, message));
        }
        // :fatcat.c0la.se PING narwhalirc
        else if ("PING".equalsIgnoreCase(comp[1])) {
            sendMessage("PONG :" + comp[2]);
        }
        else if ("PING".equalsIgnoreCase(comp[0])) {
            sendMessage("PONG :" + comp[1]);
        }
        else {
            SwingUtilities.invokeLater(new PostLog(frame, message));
        }
    }
}