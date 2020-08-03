package org.springbus.j3d;

import org.jooq.RowN;

import java.awt.*;

public class DiamonFrame extends BasicFrame {

  public static void main(String[] args) {
    DiamonFrame frame = new DiamonFrame();
    frame.run(args);
  }

  int Round(double t) {
    return    (int) (t + 0.5);
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.blue);
    super.paint(g);
    int n = 20;
    double thta = 2 * Math.PI / n;
    int r = getHeight()/3;
    Vector2[] pts = new Vector2[n];
    for (int i = 0; i < n; i++) {
      pts[i] = new Vector2();
      pts[i].setX(r * Math.cos(i * thta));
      pts[i].setY(r * Math.sin(i * thta));
    }
    for (int i = 0; i < n - 2; i++) {
      for (int j = i + 1; j < n - 1; j++) {
        g.drawLine(
            Round( getWidth()/2+pts[i].getX()),getHeight()/2+ Round(pts[i].getY()),
                getWidth()/2+
                Round(pts[j].getX()),getHeight()/2+ Round(pts[j].getY()));
      }
    }
  }
}
