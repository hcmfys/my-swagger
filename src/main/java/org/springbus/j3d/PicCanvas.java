package org.springbus.j3d;

import java.awt.*;
import java.awt.image.MemoryImageSource;

public class PicCanvas extends Component {
  MemoryImageSource mis;

  protected double userMinX = -1;
  protected double userMaxX = 1;
  protected double userMinY = -1;
  protected double userMaxY = 1;

  protected double[] viewMinX;
  protected double[] viewMaxX;
  protected double[] viewMinY;
  private double[] viewMaxY;
  static final int DefaultViewPortMax = 256;
  protected int viewPortMax = DefaultViewPortMax;
  protected int viewportNum = 0;
  protected int currentViewPort = 0;
  static final int DefaultWindowSize = 256;
  protected int windowWidth = DefaultWindowSize;
  protected int windowHeight = DefaultWindowSize;
  protected Graphics graphics;
  protected Component component;
  protected Color currentFontColor = Color.white;
  protected Color currentBackColor = Color.black;

  public PicCanvas(Component a) {
    this.component = a;
    this.graphics = a.getGraphics();
    this.windowWidth = a.getWidth();
    this.windowHeight = a.getHeight();
    createViewPort(DefaultViewPortMax);
  }

  private void createViewPort(int max) {
    currentViewPort = 0;
    viewPortMax = max;
    viewMinX = new double[viewPortMax];
    viewMaxX = new double[viewPortMax];
    viewMinY = new double[viewPortMax];
    viewMaxY = new double[viewPortMax];
    viewMinX[0] = viewMinY[0] = 0;
    viewMaxX[0] = viewMaxY[0] = 0;
    viewportNum = 1;
  }

  public void setViewPort(double x1, double x2, double y1, double y2) {
    viewMinX[viewportNum] = x1;
    viewMinY[viewportNum] = y1;
    viewMaxX[viewportNum] = x2;
    viewMaxY[viewportNum] = y2;
    currentViewPort = viewportNum;
    viewportNum++;
  }

  public void resetViewPort() {
    currentViewPort = 0;
    viewMinX[0] = viewMinY[0] = 0.0;
    viewMaxX[0] = viewMaxY[0] = 1.0;
    viewportNum = 1;
  }

  public int getIntX(double x) {
    return (int) (this.windowWidth * x);
  }

  public int getIntY(double y) {
    return (int) (windowHeight * (1 - y));
  }

  public double viewX(double x) {
    double s = (x - userMinX) / (userMaxX - userMinX);
    double t =
        viewMinX[currentViewPort] + s * (viewMaxX[currentViewPort] - viewMinX[currentViewPort]);
    return t;
  }

  public double viewY(double y) {
    double s = (y - userMinY) / (userMaxY - userMinY);
    double t =
        viewMinY[currentViewPort] + s * (viewMaxY[currentViewPort] - viewMinY[currentViewPort]);
    return t;
  }

  public int getX(double x) {
    double xx = viewX(x);
    int ix = getIntX(xx);
    return ix;
  }

  public int getY(double y) {
    double yy = viewY(y);
    int iy = getIntY(yy);
    return iy;
  }
}
