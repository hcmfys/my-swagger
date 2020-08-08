package org.springbus.comutergraphics.CG.common;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public abstract class JApplet extends JFrame {
  protected JComponent self;

  public abstract void init();

  public JApplet() {

    this.setSize(800, 400);

    this.setPreferredSize(new Dimension(600, 400));

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int px = screen.width / 2 - this.getWidth() / 2;
    int py = screen.height / 2 - this.getHeight() / 2;
    this.setLocation(px, py);
    this.setVisible(true);
  }

  public void display() {
    init();
    repaint();
  }

  public Image getImage(URL url, String filename) {
    try {
      return ImageIO.read(new FileInputStream(url.getFile()));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Image getImage(URL url) {
    try {
      return ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public URL getDocumentBase() {
    return getClass().getResource("");
  }

  public URL getCodeBase() {
    return getClass().getResource("");
  }

  public String getParameter(String name) {
    return null;
  }

  public void setPixel(Graphics g, int x, int y, Color c) {
    Color oldColor = g.getColor();
    g.setColor(c);
    g.fillOval(x, y, 2, 2);
    g.setColor(oldColor);
  }

  private  int round(double r){
    return (int) Math.floor (r+0.5d);
  }
  public void drawLine(Graphics g, double x1, double y1, double x2, double y2, Color c) {
    double k=(y2-y1)/(x2-x1+ 0.000001);
    if(k>1){
      double di=1-0.5*k;
      double xi;
      double minY;
      double maxY;
      if(y1>y2){
        xi=x2;
        minY=y2;
        maxY=y1;
      }else{
        xi=x1;
        minY=y1;
        maxY=y2;
      }
      for(double i=minY;i<=maxY;i++){
        //System.out.println("x=" +xi +" y="+i);
        setPixel(g, round(xi), round(i), c);
        if(di<0){
          xi=xi;
          di=di+1;
        } else {
          xi = xi + 1;
          di= di+1-k;
        }

      }
    }else if(k>=0 && k<=1){
      double di=0.5-k;
      double yi;
      double minX;
      double maxX=0;
      if(x1>x2){
        yi=y2;
        minX=x2;
        maxX=x1;
      }else{
        yi=y1;
        minX=x1;
        maxX=x2;
      }
      for(double i=minX;i<=maxX;i++){
        setPixel(g, round(i), round(yi), c);
        if(di<0){
          yi=yi+1;
          di=di+1-k;
        }else{
          yi=yi;
          di=di-k;
        }
      }

    }else if(k>=-1 && k<0){
      double di=-0.5-k;
      double yi;
      double minX;
      double maxX;
      if(x1>x2){
        yi=y2;
        minX=x2;
        maxX=x1;
      }else{
        yi=y1;
        minX=x1;
        maxX=x2;
      }
      for(double i=minX;i<=maxX;i++){
        setPixel(g, round(i), round(yi), c);
        if(di<0) {
          di=di-k;
          yi=yi;
        }else{
          di=di -1 -k;
          yi=yi-1;
        }
      }
    }else if(k< -1 ){

      double di=-1-0.5 *k;

      double xi;
      double minY;
      double maxY;
      if(y1>y2){
        xi=x1;
        minY=y2;
        maxY=y1;
      }else{
        xi=x2;
        minY=y1;
        maxY=y2;
      }
      for(double y=maxY;y>=minY;y--){
        setPixel(g,round( xi), round(y), c);

        if(di<0){
          xi=xi+1;
          di=di-1-k;
        }else{
          xi=xi;
          di=di-1;
        }
      }
    }
  }


  public  void drawArc(Graphics g,double x1,double r,Color c){

    double di=1.25 -r;
    double yi=r +x1;
    for(double xi=x1;xi<=x1+r;xi++) {
      setPixel(g, round(xi), round(yi), c);
      System.out.println(round( xi)+" y="+ round(yi));
      if(di<0){
        di=di+2 *xi +3;
        yi=yi;
      }else{
        di=di+2 * ( xi -yi) +5;
        yi=yi -1;
      }
    }
  }


}
