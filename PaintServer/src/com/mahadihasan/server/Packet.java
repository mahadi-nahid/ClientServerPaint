package com.mahadihasan.server;


/**
 *
 * @author Md Mahadi Hasan
 */

import java.awt.Color;

 public  class Packet implements java.io.Serializable{

   // public boolean flag;
       public Color color;
       public int x,y,a,b,x1[],y1[];
       public diffrentShapes shape;

       public Packet(diffrentShapes shape,Color color,int x,int y,int a,int b)
       {
           //  this.flag=true;
                this.shape=shape;
                this.color=color;
                this.x=x;
                this.y=y;
                this.a=a;
                this.b=b;
                System.out.println(shape.toString() + "  "+ color.toString());
       }

       public Packet(diffrentShapes shape,Color color,int x1[],int y1[],int a,int b)
       {
           //  this.flag=false;
                this.shape=shape;
                this.color=color;
                this.x=0;
                this.y=0;
                this.x1 = new int[10];
                this.y1 = new int[10];
                this.x1=x1;
                this.y1=y1;
                this.a=a;
                this.b=b;
                System.out.println(shape.toString() + "  "+ color.toString());
       }

}
