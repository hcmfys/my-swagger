package org.springbus.j3d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class MyCube extends JFrame implements  MouseMotionListener {

    float X[], Y[], Z[]; //顶点坐标

    float SX[], SY[]; //投影坐标点(屏幕坐标点)

    float LineLength; //边长

    static final double PI = 3.1416;

    int prevX, prevY;

    public static void main(String[] args) {
        MyCube my =  new MyCube();
        my.setSize(new Dimension(500, 400));
        my.init();

        my.setVisible(true);
        my.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void init() {

        int h = this.getHeight();//获取显示区的高与宽

        int w = this.getWidth();

        if (w < h)                 //取其小者

            h = w;

        LineLength = (float) (0.5 * h);          //正方体边长为h的0.5

        createCube(LineLength); //调用自定义函数,创建一个边长为LineLength的正方体


        //在我们创建正方体的物体坐标系中, 正方体的一个顶点(序号为1)在局部坐标系的原点上
        //为了让正方体围绕中心旋转,首先须进行"原点平移",
        //平移的向量为-(LineLength/2,LineLength/2,LineLength/2);

        mymove(-LineLength / 2, -LineLength / 2, -LineLength / 2);

        //以下分别调用自定义的绕y轴和x轴旋转的函数,
        //否则初始显示的画面将是没有立体感的一个正方体
        //(类似于正对着一个正方体,只能看到其中的一个面)

        yrot(20);

        xrot(20);

        addMouseMotionListener(this);

    }


    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        System.arraycopy(X, 0, SX, 0, X.length);//不能用SX=X

        System.arraycopy(Y, 0, SY, 0, Y.length);

        mymoveS(SX, SY, this.getWidth() / 2, this.getHeight() / 2);//将图形移动到显示区中心显示

        //以下将相邻的顶点

        for (int i = 1; i <= 3; i++)

            g.drawLine((int) SX[i], (int) SY[i], (int) SX[i + 1], (int) SY[i + 1]);//循环画三条线

        g.drawLine((int) SX[4], (int) SY[4], (int) SX[1], (int) SY[1]);//终点与起始点相接


        for (int i = 5; i <= 7; i++)

            g.drawLine((int) SX[i], (int) SY[i], (int) SX[i + 1], (int) SY[i + 1]);

        g.drawLine((int) SX[8], (int) SY[8], (int) SX[5], (int) SY[5]);

        g.drawLine((int) SX[1], (int) SY[1], (int) SX[5], (int) SY[5]);

        g.drawLine((int) SX[2], (int) SY[2], (int) SX[6], (int) SY[6]);

        g.drawLine((int) SX[3], (int) SY[3], (int) SX[7], (int) SY[7]);

        g.drawLine((int) SX[4], (int) SY[4], (int) SX[8], (int) SY[8]);


        //在状态栏显示顶点1的坐标值

        System.out.println("顶点1的当前坐标值 X:" + (int) SX[1] + " Y:" + (int) SY[1]);


    }


    public void mouseClicked(MouseEvent e) {

        //  yrot(10);

        //  repaint();

    }


    public void mousePressed(MouseEvent e) {
        prevX = e.getX();
        prevY = e.getY();
        e.consume();//不再执行原来(父类)的方法
        repaint();
    }





    public void mouseDragged(MouseEvent e) {

        int x = e.getX();

        int y = e.getY();

        //鼠标左右移动(x轴坐标改变),绕Y轴转动. 鼠标移动的距离等于显示区域宽度时,转动180度
        float thetaY = (x - prevX) * 180.0f / getSize().width;
        //鼠标上下移动(Y轴坐标改变),绕X轴转动 .鼠标移动的距离等于显示区域高度时,转动180度
        float thetaX = (y - prevY) * 180.0f / getSize().height;
        xrot(thetaX);
        yrot(thetaY);
        prevX = x; //此两句很关键,每计算完一次,都须将当前点作为起始点
        prevY = y;
        repaint();
        e.consume();
    }


    public void mouseMoved(MouseEvent e) {

    }


    public void disponse() {
       // removeMouseListener(this);
        removeMouseMotionListener(this);

    }


    public void createCube(float hh) {

        X = new float[9]; //坐标数组.共8个点,定义9个,下标的为0的不用

        Y = new float[9];

        Z = new float[9];


        SX = new float[9];

        SY = new float[9];

//以下确定8个顶点的坐标

        X[1] = 0; //左下角

        Y[1] = 0;

        Z[1] = 0;

        X[2] = hh;//右下角

        Y[2] = 0;

        Z[2] = 0;

        X[3] = hh; //右上角

        Y[3] = hh;

        Z[3] = 0;

        X[4] = 0; //左上角.以上四点,从原点开始,按反时针顺序,在X0Y平面上确定四个点

        Y[4] = hh;

        Z[4] = 0;

        X[5] = 0; //左下角.以下四点

        Y[5] = 0;

        Z[5] = hh;

        X[6] = hh; //右下角

        Y[6] = 0;

        Z[6] = hh;

        X[7] = hh; //右上角

        Y[7] = hh;

        Z[7] = hh;

        X[8] = 0; //左上角

        Y[8] = hh;

        Z[8] = hh;

    }


//屏幕坐标点平移

    public void mymoveS(float sx[], float sy[], float movX, float movY) {

//平移

        for (int i = 1; i <= 8; i++) {

            sx[i] += movX;

            sy[i] += movY;

        }

    }


//坐标点平移

    public void mymove(float movX, float movY, float movZ) {

//平移

        for (int i = 1; i <= 8; i++) {

            X[i] += movX;

            Y[i] += movY;

            Z[i] += movZ;

        }

    }


    public void xrot(float theta) {

//绕X轴旋转

        theta *= (PI / 180);//化为弧度

        double mycos = Math.cos(theta);

        double mysin = Math.sin(theta);

        double x, y, z;


        for (int i = 1; i <= 8; i++) {

            x = X[i];

            y = Y[i];

            z = Z[i];

            Y[i] = (float) (y * mycos - z * mysin);

            Z[i] = (float) (y * mysin + z * mycos);


        }

    }


    public void yrot(float theta) {

//绕Y轴旋转

        theta *= (PI / 180);

        double mycos = Math.cos(theta);

        double mysin = Math.sin(theta);

        double x, y, z;


        for (int i = 1; i <= 8; i++) {

            x = X[i];

            y = Y[i];

            z = Z[i];

            X[i] = (float) (x * mycos - z * mysin);

            Z[i] = (float) (x * mysin + z * mycos);

        }

    }


}


