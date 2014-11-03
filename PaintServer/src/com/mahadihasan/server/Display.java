package com.mahadihasan.server;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
import javax.swing.JPanel;

public class Display extends JPanel{

    Vector<Packet>vp = new Vector();
    private static final int port = 9001;

    public Display() {
        setSize(new Dimension(700,600));
        setPreferredSize(new Dimension(700,600));

   }  

    public void updatePackets(Packet packet) {
        vp.add(packet);
        repaint();
    }
   public Packet[] getPackets() {
       Packet[] pr = new Packet[vp.size()];
       System.out.println("packet has found "+vp.size());
        for(int i=0;i<vp.size();i++)
        {
           pr[i] = vp.get(i);
        }
       return pr;
    }
  public void paint(Graphics gr) {

        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

            for (int i = 0; i < vp.size(); i++) 
            {
                Packet pc=vp.get(i);

                if(pc.shape==diffrentShapes.LINE)
                {
                  //  System.out.println("Line");
                    g.setColor(pc.color);
                    g.drawLine(pc.x,pc.y,pc.a,pc.b);
                }

                else if(pc.shape==diffrentShapes.OVAL)
                {
                    //System.out.println("Oval");
                    g.setColor(pc.color);
                    g.drawOval(pc.x,pc.y,pc.a,pc.b);
                }

                else if(pc.shape==diffrentShapes.RECT)
                {
                   // System.out.println("Rect");
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
                //    System.out.println("Rect_fill");
                    g.setColor(pc.color);
                    g.fillRect(pc.x, pc.y,pc.a, pc.b);
                }
            }
    }
}
