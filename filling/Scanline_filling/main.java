package Scanline_filling;


import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Stack;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class main extends JPanel {
    public static class location {
        public int x;
        public int y;
    }

    public class classAndArray {
        public List<Integer> list = new ArrayList<Integer>();
        public classAndArray(){
        }
        public void listSort() {
            Collections.sort(list);
        }
    }

    public class Node {    //新编表记录x,dx,yMax
        public int x;
        public float dx;
        public int yMax;
        public Node next;
        public int ymin;
        public Node(int x, int dx, int yMax){
            this.x=x;
            this.dx=dx;
            this.yMax=yMax;
        }
        public void getYmin(int Ymin){
            this.ymin=Ymin;
        }
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
            Node [] head =new Node [Ymax];    //建立对应扫描边数的链表长度
            for(int i=0;i<index-1;i++)
            {
                if(b[i]<b[i+1])          //从第一个点开始若前一个点小于后一个点
                {
                    if(head[b[i]]==null)
                        head[b[i]]=new Node(0,0,0);
                    head[b[i]].ymin=b[i];
                    if(head[b[i]].next==null)    //该点是第一个插入的节点
                    {
                        head[b[i]].next=new Node(0,0,0);
                        head[b[i]].next.x=a[i];
                        head[b[i]].next.dx=(float)(a[i]-a[i+1])/(b[i]-b[i+1]);
                        head[b[i]].next.yMax=b[i+1];    //ymax为后一点的y
                    }
                    else {                //该点不是第一个插入的节点
                        if(head[b[i]].next.next==null)
                            head[b[i]].next.next=new Node(0,0,0);
                        if((float)(a[i]-a[i+1])/(b[i]-b[i+1])<head[b[i]].next.dx)  //当前插入x比之前存在的节点x小
                        {
                            head[b[i]].next.next.x=head[b[i]].next.x;
                            head[b[i]].next.next.dx=head[b[i]].next.dx;
                            head[b[i]].next.next.yMax=head[b[i]].next.yMax;
                            head[b[i]].next.x=a[i];
                            head[b[i]].next.dx=(float)(a[i]-a[i+1])/(b[i]-b[i+1]);
                            head[b[i]].next.yMax=b[i+1];
                        }
                        else
                        {
                            head[b[i]].next.next.x=a[i];
                            head[b[i]].next.next.dx=(float)(a[i]-a[i+1])/(b[i]-b[i+1]);
                            head[b[i]].next.next.yMax=b[i+1];
                        }
                    }
                }
                else
                {
                    if(head[b[i+1]]==null)
                        head[b[i+1]]=new Node(0,0,0);
                    head[b[i+1]].ymin=b[i+1];
                    if(head[b[i+1]].next==null)    //该点是第一个插入的节点
                    {
                        head[b[i+1]].next=new Node(0,0,0);
                        head[b[i+1]].next.x=a[i+1];
                        head[b[i+1]].next.dx=(float)(a[i]-a[i+1])/(b[i]-b[i+1]);
                        head[b[i+1]].next.yMax=b[i];    //ymax为后一点的y
                    }
                    else {                //该点不是第一个插入的节点
                        if(head[b[i+1]].next.next==null)
                            head[b[i+1]].next.next=new Node(0,0,0);
                        if((float)(a[i]-a[i+1])/(b[i]-b[i+1])<head[b[i+1]].next.dx)  //当前插入x比之前存在的节点x小
                        {
                            head[b[i+1]].next.next.x=head[b[i+1]].next.x;
                            head[b[i+1]].next.next.dx=(float)head[b[i+1]].next.dx;
                            head[b[i+1]].next.next.yMax=head[b[i+1]].next.yMax;
                            head[b[i+1]].next.x=a[i+1];
                            head[b[i+1]].next.dx=(float)(a[i]-a[i+1])/(b[i]-b[i+1]);
                            head[b[i+1]].next.yMax=b[i];
                        }
                        else
                        {
                            head[b[i+1]].next.next.x=a[i+1];
                            head[b[i+1]].next.next.dx=(float)(a[i]-a[i+1])/(b[i]-b[i+1]);
                            head[b[i+1]].next.next.yMax=b[i];
                        }
                    }
                }
            }
            if(index>0)
            {  if(b[0]<b[index-1])          //从第一个点到最后一个点
            {
                if(head[b[0]]==null)
                    head[b[0]]=new Node(0,0,0);
                head[b[0]].ymin=b[0];
                if(head[b[0]].next==null)    //该点是第一个插入的节点
                {
                    head[b[0]].next=new Node(0,0,0);
                    head[b[0]].next.x=a[0];
                    head[b[0]].next.dx=(float)(a[0]-a[index-1])/(b[0]-b[index-1]);
                    head[b[0]].next.yMax=b[index-1];    //ymax为后一点的y
                }
                else {                //该点不是第一个插入的节点
                    if(head[b[0]].next.next==null)
                        head[b[0]].next.next=new Node(0,0,0);
                    if((float)(a[0]-a[index-1])/(b[0]-b[index-1])<head[b[0]].next.dx)  //当前插入x比之前存在的节点x小
                    {
                        head[b[0]].next.next.x=head[b[0]].next.x;
                        head[b[0]].next.next.dx=head[b[0]].next.dx;
                        head[b[0]].next.next.yMax=head[b[0]].next.yMax;
                        head[b[0]].next.x=a[0];
                        head[b[0]].next.dx=(float)(a[0]-a[index-1])/(b[0]-b[index-1]);
                        head[b[0]].next.yMax=b[index-1];
                    }
                    else
                    {
                        head[b[0]].next.next.x=a[0];
                        head[b[0]].next.next.dx=(float)(a[0]-a[index-1])/(b[0]-b[index-1]);
                        head[b[0]].next.next.yMax=b[index-1];
                    }
                }
            }
            else
            {
                if(head[b[index-1]]==null)
                    head[b[index-1]]=new Node(0,0,0);
                head[b[index-1]].ymin=b[index-1];
                if(head[b[index-1]].next==null)    //该点是第一个插入的节点
                {
                    head[b[index-1]].next=new Node(0,0,0);
                    head[b[index-1]].next.x=a[index-1];
                    head[b[index-1]].next.dx=(float)(a[0]-a[index-1])/(b[0]-b[index-1]);
                    head[b[index-1]].next.yMax=b[0];    //ymax为后一点的y
                }
                else {                //该点不是第一个插入的节点
                    if(head[b[index-1]].next.next==null)
                        head[b[index-1]].next.next=new Node(0,0,0);
                    if((float)(a[0]-a[index-1])/(b[0]-b[index-1])<head[b[index-1]].next.dx)  //当前插入x比之前存在的节点x小
                    {
                        head[b[index-1]].next.next.x=head[b[index-1]].next.x;
                        head[b[index-1]].next.next.dx=head[b[index-1]].next.dx;
                        head[b[index-1]].next.next.yMax=head[b[index-1]].next.yMax;
                        head[b[index-1]].next.x=a[index-1];
                        head[b[index-1]].next.dx=(float)(a[0]-a[index-1])/(b[0]-b[index-1]);
                        head[b[index-1]].next.yMax=b[0];
                    }
                    else
                    {
                        head[b[index-1]].next.next.x=a[index-1];
                        head[b[index-1]].next.next.dx=(float)(a[0]-a[index-1])/(b[0]-b[index-1]);
                        head[b[index-1]].next.next.yMax=b[0];
                    }
                }
            }
            }
            for(int i=0;i<Ymax;i++)
                if(head[i]!=null)
                    while(head[i].next!=null)
                    {  System.out.println("新编表y"+head[i].ymin+"新编表x"+head[i].next.x+"新编表dx"+head[i].next.dx+"新编表yMax"+head[i].next.yMax);
                        if(head[i].next.next!=null)
                        {
                            System.out.println("多的"+"新编表y"+head[i].ymin+"新编表x"+head[i].next.next.x+"新编表dx"+head[i].next.next.dx+"新编表yMax"+head[i].next.next.yMax);
                        }
                        break;
                    }
            int YMIN=b[0];
            for(int i=0;i<b.length;i++)
            {
                if(YMIN>b[i]&&b[i]!=0)
                    YMIN=b[i];
            }
            classAndArray [] ca=new classAndArray [Ymax];
            for(int i=YMIN;i<Ymax;i++)
                ca[i]=new classAndArray();
            //一个点一个点的全装入ca中再排序打印出点
            for(int i=0;i<Ymax;i++)
            {
                if(head[i]!=null)
                    if(head[i].next!=null)
                    {
                        //System.out.println("新编表y"+head[i].ymin+"新编表x"+head[i].next.x+"新编表dx"+head[i].next.dx+"新编表yMax"+head[i].next.yMax);
                        for(int j=head[i].ymin;j<head[i].next.yMax;j++)
                        {
                            ca[i+j-head[i].ymin].list.add(head[i].next.x+(int)(0.5+((j-head[i].ymin)*head[i].next.dx)));
                            //System.out.print("ca[i+j-head[i].ymin]为"+(i+j-head[i].ymin)+"值为"+ca[i+j-head[i].ymin].list.toString());
                            //System.out.println("Ymin为"+i+" x为"+(head[i].next.x+(j-head[i].ymin)*head[i].next.dx));
                        }
                        if(head[i].next.next!=null)
                        {
                            for(int j=head[i].ymin;j<head[i].next.next.yMax;j++)
                            {
                                ca[i+j-head[i].ymin].list.add(head[i].next.next.x+(int)(0.5+(j-head[i].ymin)*head[i].next.next.dx));
                                //System.out.print("next中ca[i+j-head[i].ymin]为"+(i+j-head[i].ymin)+"值为"+ca[i+j-head[i].ymin].list.toString());
                                //System.out.println("Ymin为"+i+" x为"+head[i].next.next.x+(j-head[i].ymin)*head[i].next.next.dx);
                            }
                            //System.out.println("多的"+"新编表y"+head[i].ymin+"新编表x"+head[i].next.next.x+"新编表dx"+head[i].next.next.dx+"新编表yMax"+head[i].next.next.yMax);
                        }
                    }
            }
//
            for(int i=YMIN;i<Ymax;i++)
            {
                ca[i].listSort();
                for (int j = 0; j < ca[i].list.size(); j++) {
                    if(j%2==0||(j==0))
                    {
                        g2d.setColor(Color.red);
                        g2d.drawLine(ca[i].list.get(j), i, ca[i].list.get(j+1), i);
                    }
                }
                System.out.println(ca[i].list.toString());
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
        frame.setTitle("scanline_filling");

        frame.setLayout(null);

        frame.setContentPane(new main());

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}