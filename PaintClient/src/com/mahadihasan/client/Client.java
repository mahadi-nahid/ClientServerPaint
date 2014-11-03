package com.mahadihasan.client;



//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.EnumSet;
import java.util.Vector;
import javax.swing.*;

public class Client extends JPanel implements Runnable {

   
    private InputStream in = null;
    private ObjectInputStream ois = null;
    private OutputStream out = null;
    private UserInfo userInfo = null;
    private ObjectOutputStream oos = null;
    private int counter = 0;
    JButton button_new,button_save,button_pencil,button_eraser,button_brush,button_color,button_line,button_rect,button_circle,button_help,button_exit;
  //  static JFrame frame;
    Handler handler=new Handler();
    Color currentColor = Color.BLACK,fillColor = Color.WHITE;
    boolean dragging,flag=false,fill;
    int startX, startY,currentX, currentY,prevX, prevY;
    BufferedImage buffer;
    private Vector<Packet> vp = new Vector();


    public enum shape {
        CURVE, LINE, RECT, OVAL, FILLED_RECT, FILLED_OVAL
    }

    public final static EnumSet<shape> SHAPE_TOOLS = EnumSet.range(shape.LINE, shape.FILLED_OVAL);
    
    shape currentshape = shape.CURVE; 

    private enum tool {
        pencil, eraser, brush
    }

    private final static EnumSet<tool> tools = EnumSet.range(tool.pencil, tool.brush);
    tool currenttool = tool.pencil;

    static Client content;

    public static void main(String[] args)
    {
        String port_num,ip_add;
        int port=0;
        ip_add  = JOptionPane.showInputDialog("Enter IP Address of the Server");
        port_num  =   JOptionPane.showInputDialog("Enter Port Number");
       try{
           port = Integer.parseInt(port_num);
       }catch(Exception exx){}

        content = new Client();

        try {
            content.initializeSetup(ip_add,port);
         //   content.initializeSetup("127.0.0.1", 9001);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Connection can't be established. Server not Found.");
            System.exit(0);
        }

        JFrame frame = new JFrame("Client");
        frame.setContentPane(content);
        frame.setJMenuBar(content.getMenuBar());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public Client()
    {
        fill=false;
        setPreferredSize(new Dimension(600, 600));
        MouseHandler shape = new MouseHandler();
        addMouseListener(shape);
        addMouseMotionListener(shape);
      
    }

    public void initializeSetup(String ip,int port) throws Exception
    {
        System.out.println(ip+"   "+port);
         String serverAddress = ip;
         Socket socket = new Socket(serverAddress, port);
         out = socket.getOutputStream();
         oos = new ObjectOutputStream(out);
         in = socket.getInputStream();
         ois = new ObjectInputStream(in);
         new Thread(this).start();
   }

    public void run() {
        try {
            userInfo = new UserInfo();

            Packet pc[];
            userInfo.sendMessage("Send Info");
            userInfo.setUser("Mahi");
            oos.writeObject(userInfo);
            oos.flush();
            System.out.println("Connection established");

            while (true)
            {
               pc = (Packet[])ois.readObject();
               if(pc!=null)
               {
                   updatePackets(pc);
                  
               }
            }

        } catch (Exception ex) {
            System.out.println("Exception In run of new Client "+ ex);
        }finally{
            try {
                ois.close();
                oos.close();
            } catch (Exception ex) {
               
            }
        }
    }

     public  void updatePackets(Packet packet) {
      vp.add(packet);
      drawing = true;
      repaint();
    }
    public void updatePackets(Packet[] packet)
    {
        vp.clear();
        for(int i=0;i<packet.length;i++)
        {
            vp.add(packet[i]);
        }
        drawing = true;
        repaint();
    }

    private JMenuBar getMenuBar() {

        JMenuBar menubar = new JMenuBar();

        button_save = new JButton();
        Image img_save = Toolkit.getDefaultToolkit().getImage("images/save.png");
        button_save.setIcon(new ImageIcon(img_save));
        button_save.setToolTipText("Save File");
        button_save.addActionListener(handler);
        menubar.add(button_save);


        button_pencil = new JButton();
        Image img_pencil = Toolkit.getDefaultToolkit().getImage("images/pencil.png");
        button_pencil.setIcon(new ImageIcon(img_pencil));
        button_pencil.setToolTipText("Pencil");
        button_pencil.addActionListener(handler);
        menubar.add(button_pencil);

        button_eraser= new JButton();
        Image img_eraser = Toolkit.getDefaultToolkit().getImage("images/eraser.png");
        button_eraser.setIcon(new ImageIcon(img_eraser));
        button_eraser.setToolTipText("Eraser");
        button_eraser.addActionListener(handler);
        menubar.add(button_eraser);

        button_brush = new JButton();
        Image img_brush = Toolkit.getDefaultToolkit().getImage("images/brush.png");
        button_brush.setIcon(new ImageIcon(img_brush));
        button_brush.setToolTipText("Brush");
        button_brush.addActionListener(handler);
        menubar.add(button_brush);

        button_new = new JButton();
        Image img_new = Toolkit.getDefaultToolkit().getImage("images/new.png");
        button_new.setIcon(new ImageIcon(img_new));
        button_new.setToolTipText("Fill or Not Fill");
        button_new.addActionListener(handler);
        menubar.add(button_new);

        button_color = new JButton();
        Image img_color = Toolkit.getDefaultToolkit().getImage("images/color.png");
        button_color.setIcon(new ImageIcon(img_color));
        button_color.setToolTipText("Choose Color");
        button_color.addActionListener(handler);
        menubar.add(button_color);

        button_line = new JButton();
        Image img_line = Toolkit.getDefaultToolkit().getImage("images/line.png");
        button_line.setIcon(new ImageIcon(img_line));
        button_line.setToolTipText("Draw a Line");
        button_line.addActionListener(handler);
        menubar.add(button_line);


        button_rect = new JButton();
        Image img_rect = Toolkit.getDefaultToolkit().getImage("images/rect.png");
        button_rect.setIcon(new ImageIcon(img_rect));
        button_rect.setToolTipText("Draw a Rectangle");
        button_rect.addActionListener(handler);
        menubar.add(button_rect);


        button_circle = new JButton();
        Image img_circle = Toolkit.getDefaultToolkit().getImage("images/circle.png");
        button_circle.setIcon(new ImageIcon(img_circle));
        button_circle.setToolTipText("Draw a Oval");
        button_circle.addActionListener(handler);
        menubar.add(button_circle);

        button_help = new JButton();
        Image img_help = Toolkit.getDefaultToolkit().getImage("images/help.png");
        button_help.setIcon(new ImageIcon(img_help));
        button_help.setToolTipText("Help");
        button_help.addActionListener(handler);
        menubar.add(button_help);


        button_exit = new JButton();
        Image img_exit = Toolkit.getDefaultToolkit().getImage("images/exit.png");
        button_exit.setIcon(new ImageIcon(img_exit));
        button_exit.setToolTipText("Close");
        button_exit.addActionListener(handler);
        menubar.add(button_exit);

        return menubar;
    }


     private class Handler implements ActionListener {

        public void actionPerformed(ActionEvent e)
        {
            System.out.println("done");
            if(e.getSource()==button_new) {
                
               if(fill==true) fill=false;

               else fill=true;

            }

            if(e.getSource()==button_save) { 
                
                JFileChooser fChoose = new JFileChooser();
                fChoose.showOpenDialog(null);
                File file = fChoose.getSelectedFile();

                if (file != null) {
                    savePic(content, file.toString() + ".jpg");
          
                    JOptionPane.showMessageDialog(null, "Image Saved", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                System.out.printf("save\n",e.getActionCommand());
            }


            if(e.getSource()==button_pencil)
            {
                flag=true;
                currenttool = tool.pencil;
                currentshape=shape.CURVE;
                System.out.printf("pen\n",e.getActionCommand());
            }

            if(e.getSource()==button_eraser) {

                 flag=true;
                 currentshape = null;
                 currenttool = tool.eraser;
                 System.out.printf("eraser\n",e.getActionCommand());
            }

            if(e.getSource()==button_brush) {
                 flag=true;
                 currentshape = null;
                 currenttool = tool.brush;
                 System.out.printf("brush %s\n",e.getActionCommand());
            }

            if(e.getSource()==button_color) {
                
                Color newColor = JColorChooser.showDialog(Client.this, "Select Drawing Color", currentColor);

                if (newColor != null) {
                    currentColor = newColor;
                }
                
                System.out.println("color  "+ currentColor);
            }

            if(e.getSource()==button_line) {
                
                flag = false;
                currenttool = tool.pencil;
                currentshape = shape.LINE;
                System.out.printf("line\n",e.getActionCommand());
            }

            if(e.getSource()==button_rect) {

                flag = false;
                currenttool = tool.pencil;
                currentshape = shape.RECT;
                System.out.printf("rect\n",e.getActionCommand());
            }

            if(e.getSource()==button_circle)  {

                flag = false;
                currenttool = tool.pencil;
                currentshape = shape.OVAL;
                System.out.printf("circle\n",e.getActionCommand());
            }

            if(e.getSource()==button_help) {
                
                 try{
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+"Help.doc");
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, e);
                }
                System.out.printf("help\n",e.getActionCommand());
            }

            if(e.getSource()==button_exit) System.exit(0);
        }
    }


    private class MouseHandler implements MouseListener, MouseMotionListener {


        public void mousePressed(MouseEvent e) 
        {
            startX = prevX = currentX = e.getX();
            startY = prevY = currentY = e.getY();
            dragging = true;
        }

        public void mouseDragged(MouseEvent e) 
        {
            currentX = e.getX();
            currentY = e.getY();

             if (SHAPE_TOOLS.contains(currentshape) ) // flag == false)  // only to show shape while drawing rect /line
            {
                repaintRect(startX, startY, prevX, prevY);                            //no permanent
                repaintRect(startX, startY, currentX, currentY);
            }
            
            else if (currenttool == tool.eraser)
            {
                dragging = false;
                Graphics g = buffer.getGraphics();
                g.setColor(fillColor);

                Packet p=new Packet(diffrentShapes.RECT_FILL,fillColor,prevX,prevY,17,17);
                try {
                        oos.writeObject(p);
                        oos.flush();
                    } catch (Exception ex) {
                        System.err.println("Object output error: " + ex);
               }
                g.fillRect(prevX, prevY,17,17);
                repaint(prevX, prevY, currentX, currentY);
                g.dispose();
            }
            
            else if (currenttool == tool.brush)
            {
                dragging = false;
                Graphics g = buffer.getGraphics();
                g.setColor(currentColor);
       
                Packet p=new Packet(diffrentShapes.OVAL_FILL,currentColor,prevX,prevY,17,17);
                try {
                        oos.writeObject(p);
                        oos.flush();
                    } catch (Exception ex) {
                        System.err.println("Object output error: " + ex);
               }
                g.fillOval(prevX, prevY, 17, 17);
                repaint(prevX, prevY, currentX, currentY);
                g.dispose();
            }

            else if(currenttool==tool.pencil)
            {   
                Graphics g = buffer.getGraphics();
                g.setColor(currentColor);
                //packet create korte hbe
                Packet p= new Packet(diffrentShapes.LINE,currentColor,prevX, prevY,currentX,currentY);
                try {
                        oos.writeObject(p);
                        oos.flush();
                    } catch (Exception ex) {
                        System.err.println("Object output error: " + ex);
               }
                g.drawLine(prevX, prevY, currentX, currentY);
                g.dispose();
                repaintRect(prevX, prevY, currentX, currentY);
            }
            prevX = currentX;
            prevY = currentY;
        }

        public void mouseReleased(MouseEvent e)
        {
            dragging = false;

            if (SHAPE_TOOLS.contains(currentshape) && flag == false) //shape
            {
                Graphics g = buffer.getGraphics();
                g.setColor(currentColor);
                drawShape(g,true);
                g.dispose();
                repaint();
            }
//            else if (currenttool == tool.eraser && flag == true) //tools
//            {
//                Graphics g = buffer.getGraphics();
//                g.setColor(fillColor);
//                drawShape(g);
//                g.dispose();
//                repaint();
//            }
        }

        public void mouseClicked(MouseEvent e) { }

        public void mouseEntered(MouseEvent e)
        {
           //Graphics g = buffer.getGraphics();
            if(currenttool==tool.pencil)
            {
               Toolkit toolkit = Toolkit.getDefaultToolkit();
               Image image = toolkit.getImage("images/upencil.png");
               Point hotSpot = new Point(0,0);
               Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Pencil");
               setCursor(cursor); 
            }
            else if(currenttool==tool.eraser)
            {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image = toolkit.getImage("images/ueraser.png");
                Point hotSpot = new Point(0,0);
                Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "eraser");
                setCursor(cursor);
            }
            else if(currenttool==tool.brush)
            {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image = toolkit.getImage("images/ubrush.png");
                Point hotSpot = new Point(0,0);
                Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "brush");
                setCursor(cursor);
            }
        }

        public void mouseExited(MouseEvent e) { }

        public void mouseMoved(MouseEvent e) { }

    }


    private void createbuffer()
    {
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics b = buffer.getGraphics();
        b.setColor(fillColor);
        b.fillRect(0, 0, getWidth(), getHeight());
        b.dispose();
    }
private boolean drawing = false;

    public void paint(Graphics g)
    {
        if(!drawing){
        if (buffer == null) 
        {
            System.out.println("Creating Buffer");
            createbuffer();
        }

       g.drawImage(buffer, 0, 0, null);

        if (SHAPE_TOOLS.contains(currentshape) && dragging)
        {
            g.setColor(currentColor);
            drawShape(g,false);
        }
        }
        else{
            drawing = false;
             g.drawImage(buffer, 0, 0, null);
//         if (SHAPE_TOOLS.contains(currentshape) && dragging)
//        {
//            g.setColor(currentColor);
//            drawShape(g,false);
//        }
            for (int i = 0; i < vp.size(); i++)
            {
                Packet pc=vp.get(i);

                if(pc.shape==diffrentShapes.LINE)
                {
                  //  System.out.println("Line ake");
                    g.setColor(pc.color);
                    g.drawLine(pc.x,pc.y,pc.a,pc.b);
                }

                else if(pc.shape==diffrentShapes.OVAL)
                {
                 //   System.out.println("Oval ake");
                    g.setColor(pc.color);
                    g.drawOval(pc.x,pc.y,pc.a,pc.b);
                }

                else if(pc.shape==diffrentShapes.RECT)
                {
             //       System.out.println("Rect ake");
                    g.setColor(pc.color);
                    g.drawRect(pc.x,pc.y,pc.a,pc.b);
                }

                else if(pc.shape==diffrentShapes.OVAL_FILL)
                {
                    g.setColor(pc.color);
                    g.fillOval(pc.x, pc.y,pc.a, pc.b);
                }

                else if(pc.shape==diffrentShapes.RECT_FILL)
                {
             //       System.out.println("Rect_fill ake");
                    g.setColor(pc.color);
                    g.fillRect(pc.x, pc.y,pc.a, pc.b);
                }
            }
        }

    }

 
    //just calls repaint
    private void repaintRect(int x1, int y1, int x2, int y2) {
       
        if (x2 < x1) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y2 < y1) {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }

        x1--; x2++; y1--; y2++;

        repaint(x1, y1, (x2 - x1), (y2 - y1));
    }

     public void DrawRectOval(Graphics g, Boolean pack, int x1, int y1, int x2, int y2) {

        if (x1 == x2 || y1 == y2)  return ;

        if (x1 > x2)
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        if (y1 > y2)
        {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        if (currentshape == shape.OVAL )              // && fill == false)
        {
            Packet p=null;
            //have to create packet
            if(pack==true)
            {
                if(fill==false)  {
                    p=new Packet(diffrentShapes.OVAL, currentColor, x1, y1, x2 - x1, y2 - y1);
                    g.drawOval(x1, y1, x2 - x1, y2 - y1);
                }

                else  {
                    p=new Packet(diffrentShapes.OVAL_FILL, currentColor, x1, y1, x2 - x1, y2 - y1);
                    g.fillOval(x1, y1, x2 - x1, y2 - y1);
                }

                try {
                        oos.writeObject(p);
                        oos.flush();
                    } catch (Exception ex) {
                        System.err.println("Object output error: " + ex);
               }
            }
             g.drawOval(x1, y1, x2 - x1, y2 - y1);
            
        }
        else if (currentshape == shape.RECT)         // && fill == false)
        {
            Packet p=null;
            //have to create packet
            if(pack==true)
            {
                if(fill==false) {
                    p=new Packet(diffrentShapes.RECT, currentColor, x1, y1, x2 - x1, y2 - y1);
                    g.drawRect(x1, y1, x2 - x1, y2 - y1);
                }

                else {
                    p=new Packet(diffrentShapes.RECT_FILL, currentColor, x1, y1, x2 - x1, y2 - y1);
                    g.fillRect(x1, y1, x2 - x1, y2 - y1);
                }

                try {
                        oos.writeObject(p);
                        oos.flush();
                    } catch (Exception ex) {
                        System.err.println("Object output error: " + ex);
               }
            }
            g.drawRect(x1, y1, x2 - x1, y2 - y1);
            
        }
        //else if (currentshape == shape.FILLED_RECT && fill == true) { g.fillRect(x1, y1, x2 - x1, y2 - y1);}
        //else if (currentshape == shape.FILLED_OVAL && fill == true) {  g.fillOval(x1, y1, x2 - x1, y2 - y1);
    }

    //line draw and oval & rect draw using DrawRectOval()
    private void drawShape(Graphics g,Boolean pack) {

        if (currentshape == shape.LINE) 
        {
            //packet
            if(pack)
            {
                Packet p=new Packet(diffrentShapes.LINE, currentColor, startX, startY, currentX, currentY);
                try {
                        oos.writeObject(p);
                        oos.flush();
                    } catch (Exception ex) {
                        System.err.println("Object output error: " + ex);
               }
            }
            g.drawLine(startX, startY, currentX, currentY);
        } 
        else if (currentshape == shape.OVAL)
        {
            DrawRectOval(g, pack, startX, startY, currentX, currentY);
        }  
        else if (currentshape == shape.RECT)
        {
            DrawRectOval(g,  pack ,startX, startY, currentX, currentY);
        }
    //        else if (currenttool == tool.eraser)
    //        {
    //            g.setColor(fillColor);
    //            g.fillRect(currentX, currentY, 17, 17);
    //        }
        // else if (currentshape == shape.FILLED_OVAL) { DrawRectOval(g, true, startX, startY, currentX, currentY); }
        //else if (currentshape == shape.FILLED_RECT) {  DrawRectOval(g, true, startX, startY, currentX, currentY); }
    }

    
    public static void savePic(Component myComponent, String filename)
    {
        Dimension size = myComponent.getSize();
        BufferedImage myImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = myImage.createGraphics();
        myComponent.paint(g2);
        try {
            OutputStream out = new FileOutputStream(filename);
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(myImage);
            out.close();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
