package polygon_cut;

import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.Stack;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class main extends JPanel {
    public static class location {
        public int x;
        public int y;
    }


    static int a[]=new int [20];        //多边形点的x坐标
    static int b[]=new int [20];        //多边形点的y坐标
    static int c[]=new int [20];        //矩形框点的x坐标
    static int d[]=new int [20];        //矩形框点的y坐标
    static int index=0;
    static int index1=0;
    static int time=0;
    static int time2=0;
    static int time3=0;
    static boolean add;
    ArrayList<Point>allPoint=new ArrayList<Point>();
    ArrayList<Point>drawPoint=new ArrayList<Point>();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                allPoint.clear();
                //左击鼠标打点
                if(e.getButton()==e.BUTTON1) {
                    add=true;
                    if(index!=0) {
                        for(int i=0;i<index;i++) {
                            if(a[i]==e.getX()&&b[i]==e.getY())
                                add=false;
                        }
                    }
                    if(add) {
                        a[index]=e.getX();
                        b[index]=e.getY();

                        System.out.println("多边形第"+index+"个点坐标为("+a[index]+","+b[index]+")");
                        index++;

                        repaint();

                        if(time2==0)
                            time2++;
                    }
                }

                //中击鼠标闭合
                if(e.getButton()==e.BUTTON2){
                    add=true;
                    if(index1!=0) {
                        for(int i=0;i<index1;i++) {
                            if(c[i]==e.getX()&&d[i]==e.getY())
                                add=false;
                        }
                    }
                    if(add && index1<2) {
                        c[index1]=e.getX();
                        d[index1]=e.getY();

                        System.out.println("矩形框第"+index1+"个点坐标为("+c[index1]+","+d[index1]+")");
                        index1++;

                        repaint();

                        if(time3==0)
                            time3++;
                        repaint();

                    }
                }

                //右击鼠标裁剪
                if(e.getButton()==e.BUTTON3){

                    time++;
                    repaint();
                }


            }


        });

        Graphics2D g2d = (Graphics2D)g;





        /*
         * 画出多边形
         */
        if(time2!=0) {                                                  //画线
            for(int Sum=0;Sum<=index-1;Sum++) {
                if(Sum==index-1) {
                    break;
                }
                else{
                    drawLine(a[Sum], b[Sum], a[Sum+1],b[Sum+1],g2d);
                }

            }
            drawLine(a[index-1], b[index-1], a[0],b[0],g2d);
        }


        if(time3!=0) {                                                  //闭合
            if(index1==2){
                xMin=c[0];
                xMax=c[1];
                yMin=d[0];
                yMax=d[1];
                g2d.setColor(Color.blue);
                drawLine(c[0], d[0], c[1],d[0],g2d);
                drawLine(c[0], d[0], c[0],d[1],g2d);
                drawLine(c[1], d[1], c[1],d[0],g2d);
                drawLine(c[1], d[1], c[0],d[1],g2d);
            }
        }



        if(time!=0) {

            for(int Sum=0;Sum<=index-1;Sum++) {
                if(Sum==index-1) {
                    clipping_single_line(a[Sum], b[Sum], a[0],b[0],g2d);
                }
                else{
                    clipping_single_line(a[Sum], b[Sum], a[Sum+1],b[Sum+1],g2d);
                }

            }




        }


    }

    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;

    public static int xMin;
    public static int yMin;
    public static int xMax;
    public static int yMax;


    public int getRegionCode(double x, double y) {
        int xcode = x < xMin ? LEFT : x > xMax ? RIGHT : INSIDE;
        int ycode = y < yMin ? BOTTOM : y > yMax ? TOP : INSIDE;
        return xcode | ycode;
    }

    public void clipping_single_line(double p1x,double p1y,double p2x,double p2y,Graphics2D g2d){

        int code1=getRegionCode(p1x,p1y);
        int code2=getRegionCode(p2x,p2y);

        double qx = 0, qy = 0;
        boolean vertical = p1x == p2x;
        double slope = vertical ? 0d : (p2y - p1y) / (p2x - p1x);

        while (true) {
            if(code1 == INSIDE & code2 == INSIDE){
                g2d.setColor(Color.red);
                drawLine((int)p1x, (int)p1y, (int)p2x, (int)p2y,g2d);

                break;
            }

            if ((code1 & code2) != INSIDE){
                break;

            }

            int codeout = code1 == INSIDE ? code2 : code1;

            if ((codeout & LEFT) != INSIDE) {
                qx = xMin;
                qy = (qx - p1x) * slope + p1y;
            } else if ((codeout & RIGHT) != INSIDE) {
                qx = xMax;
                qy = (qx - p1x) * slope + p1y;
            } else if ((codeout & BOTTOM) != INSIDE) {
                qy = yMin;
                qx = vertical ? p1x : (qy - p1y) / slope + p1x;
            } else if ((codeout & TOP) != INSIDE) {
                qy = yMax;
                qx = vertical ? p1x : (qy - p1y) / slope + p1x;
            }

            if (codeout == code1) {
                p1x = qx;
                p1y = qy;
                code1 = getRegionCode(p1x, p1y);
            } else {
                p2x = qx;
                p2y = qy;
                code2 = getRegionCode(p2x, p2y);
            }
        }

    }





    public void drawLine(int x0,int y0,int x1,int y1,Graphics2D g) {
        int x = x0;
        int y = y0;

        int w = x1 - x0;
        int h = y1 - y0;

        int dx1 = w < 0 ? -1: (w > 0 ? 1 : 0);
        int dy1 = h < 0 ? -1: (h > 0 ? 1 : 0);

        int dx2 = w < 0 ? -1: (w > 0 ? 1 : 0);
        int dy2 = 0;

        int fastStep = Math.abs(w);
        int slowStep = Math.abs(h);
        if (fastStep <=slowStep) {
            fastStep= Math.abs(h);
            slowStep= Math.abs(w);

            dx2= 0;
            dy2= h < 0 ? -1 : (h > 0 ? 1 : 0);
        }
        int numerator = fastStep>> 1;

        for (int i = 0; i <=fastStep; i++) {

            g.drawString(".", x, y);
            Point p=new Point(x,y);
            allPoint.add(p);
            numerator+= slowStep;
            if (numerator >=fastStep) {
                numerator-= fastStep;
                x+= dx1;
                y+= dy1;
            }else {
                x+= dx2;
                y+= dy2;
            }
        }
    }






    public static void main(String[] args) throws IOException {

//        createAndShowGUI();
        JFrame frame = new JFrame();
        frame.setTitle("polygon_cut");

        frame.setLayout(null);

        frame.setContentPane(new main());

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
