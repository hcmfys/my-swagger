package org.springbus.comg.CG.common;

import javax.swing.*;
import java.awt.*;
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
    init();

    repaint();
  }

  public   Image getImage(URL url, String filename){
    return null;
  }
  public  URL getDocumentBase(){
    return null;
  }

  public  URL   getCodeBase(){
    return null;
  }

  public  String getParameter(String name){
    return null;
  }
}
