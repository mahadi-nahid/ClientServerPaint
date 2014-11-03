package com.mahadihasan.server;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import javax.swing.JFrame;

public class Handeler extends Thread{
       private Socket socket;
       private InputStream  in = null;
       private ObjectInputStream ois = null ;
       private OutputStream out = null;
       private ObjectOutputStream oos = null;
       private UserInfo userInfo = null;
       private String name;
        private int counter = 0;
        private Packet pc = null ;
        private static HashSet<String> names = new HashSet<String>();
        private  HashSet<ObjectOutputStream>  writers;
        JFrame jframe ;
        Display display;
   
        public Handeler(int i,Socket accept,Display display,HashSet writers)
        {
            this.socket=accept;
            this.counter = i;
            this.display = display;
            this.writers = writers;
            try {
                out = socket.getOutputStream();
                oos = new ObjectOutputStream(out);
            } catch (Exception ex) {
            }
      }

      public void run()
      {
           try {
                ois = new ObjectInputStream(socket.getInputStream());

               userInfo = (UserInfo)ois.readObject();

               if(userInfo!=null){

                   System.out.println("Your name is :  "+userInfo.getUserName());
                   writers.add(oos);
               }
               while(true){
               pc = (Packet)ois.readObject();
               if(pc!=null)
               {
                     display.updatePackets(pc);
                     for (ObjectOutputStream writer : writers) {
                        writer.writeObject(display.getPackets());
                    }
                }
                }
               }catch(Exception ex){
                      System.err.println("Client connection has closed. " +ex);
               }
       }
}





