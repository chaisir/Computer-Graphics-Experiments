package seed_filling;


import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.Stack;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class main extends JPanel {
    public class location {
        public int x;
        public int y;
    }

    static int a[]=new int [20];        //点的x坐标
    static int b[]=new int [20];        //点的y坐标
    static int index=0;
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
                if(e.getButton() == e.BUTTON1) {
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

                        System.out.println("第"+index+"个点坐标为("+a[index]+","+b[index]+")");
                        index++;

                        repaint();

                        if(time2==0)
                            time2++;
                    }
                }

                //中击鼠标闭合
                if(e.getButton()==e.BUTTON2){
                    if(time3==0)
                        time3++;
                    repaint();
                }

                //右击鼠标填充
                if(e.getButton()==e.BUTTON3){
                    a[index]=e.getX();
                    b[index]=e.getY();
                    System.out.println("种子点坐标为：("+a[index]+","+b[index]+")");
                    time++;
                    repaint();
                }


            }


        });

        Graphics2D g2d = (Graphics2D)g;



        int Ymax=0;
        for(int i=0;i<b.length;i++) {
            if(Ymax<b[i])
                Ymax=b[i];
        }
//        System.out.println("Ymax："+Ymax);


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
        }


        if(time3!=0) {                                                  //闭合
            drawLine(a[index-1], b[index-1], a[0],b[0],g2d);
        }



         //得到边界点
        if(time!=0) {
            boolean spanNeedFill;
            location l=new location();
            l.x=a[index];
            l.y=b[index];
            Stack<location> stack=new Stack<location>();        //将种子点压入栈
            stack.push(l);
//            int a=0;


            while(!stack.empty()) {
//                a++;
//                System.out.print("第"+a+"次");
                location lo=stack.pop();
                int x=lo.x;
                int y=lo.y;
//                if(a>50) {
//                    System.out.println("开始画上时x"+x);
//                    System.out.println("开始画上时y"+y);
//                }
                // System.out.print("y"+y);


                while(!allPoint.contains(new Point(x,y))&&!drawPoint.contains(new Point(x,y))) {    //右扫
                    drawPoint.add(new Point(x,y));
//                    System.out.println("需画点"+x+","+y);
                    g2d.setColor(Color.red);
                    g2d.drawString(".",x,y);
                    x++;
                }
                int xr=x-1;                 //xRight


                x=lo.x-1;
                while(!allPoint.contains(new Point(x,y))&&!drawPoint.contains(new Point(x,y))) {    //左扫
                    drawPoint.add(new Point(x,y));
//                    System.out.println("需画点"+x+","+y);
                    g2d.setColor(Color.red);
                    g2d.drawString(".", x,y);
                    x--;
                }
                int xl=x+1;                 //xLeft


                //处理上面的扫描线
                x=xl;
                y=y+1;

                while(x<=xr) {

                    spanNeedFill=false;
                    while(!allPoint.contains(new Point(x,y))&&!drawPoint.contains(new Point(x,y))) {
                        spanNeedFill=true;
                        x++;

                        //System.out.println("上扫描线边界为"+x);
                    }
                    if(spanNeedFill) {
                        //System.out.print("入栈1");

                        location lc=new location();
                        lc.x=x-1;
                        lc.y=y;
                        stack.push(lc);
                        //System.out.println("入栈1，此时 x="+lc.x);
                        //System.out.println("入栈1，此时 y="+lc.y);
                        spanNeedFill=false;

                    }
                    while(allPoint.contains(new Point(x,y))||drawPoint.contains(new Point(x,y))&&x<=xr)
                        x++;
                }

                //下扫描线
                x=xl;
                y=y-2;
                while(x<=xr) {
                    spanNeedFill=false;
                    while(!allPoint.contains(new Point(x,y))&&!drawPoint.contains(new Point(x,y)))
                    {
                        spanNeedFill=true;
                        x++;
                    }
                    if(spanNeedFill)
                    {
                        //System.out.print("入栈2");
                        lo.x=x-1;
                        lo.y=y;
                        stack.push(lo);
//                        System.out.print("入栈2");
                        spanNeedFill=false;

                    }
                    while(allPoint.contains(new Point(x,y))||drawPoint.contains(new Point(x,y))&&x<=xr)
                        x++;

                }
            }


//            for(int i=0;i<drawPoint.size();i++) {
//                //System.out.println("画的y"+p.y);
//                Point p=drawPoint.get(i);
//                System.out.println("填充点：（"+p.x+"，"+p.y+"）");
//                g2d.setColor(Color.red);
//                g2d.drawString(".",p.x,p.y);
//
//            }
        }


    }



//    private static void createAndShowGUI() {
//        JFrame frame = new JFrame();
//        frame.setTitle("seed_filling");
//        frame.setLocationRelativeTo(null);
//
//        frame.setLayout(null);
//        JPanel jp=new JPanel();
//        frame.setVisible(true);
//
//
//        frame.setContentPane(new main());
//
//        frame.setSize(600, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }


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
        frame.setTitle("seed_filling");
        frame.setLocationRelativeTo(null);

        frame.setLayout(null);
        JPanel jp=new JPanel();
        frame.setVisible(true);


        frame.setContentPane(new main());

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}