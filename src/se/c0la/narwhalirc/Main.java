package se.c0la.narwhalirc;
import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    throws Exception
    {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
        }
        
        MainFrame frame = new MainFrame();
        
        Network network = new Network(frame);
        network.setNick("narwhalirc");
        network.setRealName("narwhal irc 0.9");
        network.setChannel("#floodffs!");
        network.connect("irc.esper.net", 6667);
        
        frame.setNetwork(network);
    }
}
