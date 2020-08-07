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

    this.setSize(600, 400);

    this.setPreferredSize(new Dimension(600, 400));

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int px = screen.width / 2 - this.getWidth() / 2;
    int py = screen.height / 2 - this.getHeight() / 2;
    this.setLocation(px, py);
    this.setVisible(true);

  }

  public void display(){
     init();
     repaint();
  }

  public   Image getImage(URL url, String filename) {
    try {
      return ImageIO.read(new FileInputStream(url.getFile()));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public   Image getImage(URL url) {
    try {
      return ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  public  URL getDocumentBase(){
    return getClass().getResource("");
  }

  public  URL   getCodeBase(){
    return getClass().getResource("");
  }

  public  String getParameter(String name){
    return null;
  }
}
