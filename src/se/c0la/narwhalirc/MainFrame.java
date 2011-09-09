package se.c0la.narwhalirc;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    public MainFrame()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        JTextArea log = new JTextArea();
        panel.add(log);
        
        panel.add(Box.createVerticalStrut(5));
        
        JTextField input = new JTextField();
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        panel.add(input);
        
        
        add(panel);
        
        setVisible(true);
        setTitle("Narwhal IRC 0.9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        
        pack();
    }
}