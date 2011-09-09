package se.c0la.narwhalirc;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    private Network net;
    private JTextArea log;
    private JTextField input;
    
    public MainFrame()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        log = new JTextArea();
        panel.add(new JScrollPane(log));
        
        panel.add(Box.createVerticalStrut(5));
        
        input = new JTextField();
        input.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() != KeyEvent.VK_ENTER){
                    return;
                }
                net.sendMessage(input.getText());
                log(input.getText());
                input.setText("");
            }
        });
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        panel.add(input);
        
        add(panel);
        
        setVisible(true);
        setTitle("Narwhal IRC 0.9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        
        pack();
    }
    
    public void setNetwork(Network net)
    {
        this.net = net;
    }
    
    public void log(String message)
    {
        log.append(message + "\n");
    }
}