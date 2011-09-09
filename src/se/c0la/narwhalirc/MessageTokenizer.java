package se.c0la.narwhalirc;

import java.util.*;

public class MessageTokenizer
{
    public static String[] tokenize(String message)
    {
        if (message.charAt(0) == ':') {
            message = message.substring(1);
        }
    
        int idx = 0;
        String remaining = message;
        List<String> tokens = new ArrayList<String>();
        while (true) {
            idx = remaining.indexOf(" ");
            if (idx == -1) {
                if (remaining.length() == 0) {
                    // no action if the string is empty
                }
                else if (":".equals(remaining.substring(0,1))) {
                    remaining = remaining.substring(1);
                }
                tokens.add(remaining);
                break;
            }

            String token = remaining.substring(0, idx);
            if (token.length() == 0) {
                remaining = remaining.substring(idx+1);
                continue;
            }

            if (":".equals(token.substring(0,1))) {
                tokens.add(remaining.substring(1));
                break;
            }

            remaining = remaining.substring(idx+1);
            tokens.add(token);
        }

        return tokens.toArray(new String[tokens.size()]);
    }
    
    public static String getNick(String sender)
    {
        String[] firstSplit = sender.split("@");
        String[] secondSplit = firstSplit[0].split("!");
        return secondSplit[0];
    }

    public static void main(String[] args)
    {
        String[] tests = new String[] {
        "JOIN #c0la",
        "PRIVMSG #c0la :foo",
        "PRIVMSG #c0la :hello there kiddo. ;)",
        "PING",
        "HEJ D FSF FS",
        ":aderyn!emil@foo.com privmsg #c0la :i have a nice shoulder bag"
        };

        for (String test : tests) {
            System.out.println(test);
            String[] tokenized = MessageTokenizer.tokenize(test);
            for (String token : tokenized) {
                System.out.println(token);
            }

            System.out.println();
        }
    }
}
