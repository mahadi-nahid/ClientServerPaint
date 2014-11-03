package com.mahadihasan.server;

/**
 *
 * @author Md Mahadi Hasan
 */

import java.awt.*;
import java.net.*;
import java.util.HashSet;
import javax.swing.*;
import java.io.*;

public class Server {

    
    private static int counter = 0;
    private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();

    public static void main(String[] agaira) throws Exception {
     String   port_num  =   JOptionPane.showInputDialog("Enter Port Number where Server will Listen.");
     int PORT=9001;
       try{
           PORT = Integer.parseInt(port_num);
       }catch(Exception exx){}
        System.out.println("The chat server is running.");
        JFrame jframe = new JFrame("Server Frame");
        Dimension d = new Dimension(700, 600);
        jframe.setSize(700, 600);
        jframe.setPreferredSize(d);
        jframe.setLocationRelativeTo(null);
        Display display = new Display();
        jframe.add(display);
        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handeler(++counter, listener.accept(), display, writers).start();
            }
        } finally {
            listener.close();
        }
    }
}