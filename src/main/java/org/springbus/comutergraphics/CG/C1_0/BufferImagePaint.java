package org.springbus.comutergraphics.CG.C1_0;

import org.springbus.comutergraphics.CG.common.JApplet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.cert.X509Certificate;

public class BufferImagePaint extends JApplet {

  @Override
  public void init() {}

  @Override
  public void paint(Graphics g) {

    drawLine(g, 100, 200, 202, 202, Color.red);
    drawLine(g, 100, 200, 200, 400, Color.red);
    // g.setColor(Color.green);
    drawLine(g, 202, 202, 200, 400, Color.red);

    g.setColor(Color.red);

    g.drawLine(200, 200, 302, 202);
    g.drawLine(200, 200, 300, 400);
    // g.setColor(Color.green);
    g.drawLine(302, 202, 300, 400);

    drawArc(g, 200, 30, Color.red);
  }

  public static void main(String[] args) {
    BufferImagePaint bufferImagePaint = new BufferImagePaint();
    bufferImagePaint.display();
  }
}
