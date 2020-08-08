package org.springbus.comutergraphics.CG.C1_0;

import org.springbus.comutergraphics.CG.common.JApplet;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class RgbColorSelect extends JApplet implements ChangeListener {

  private JPanel rPanel = new JPanel();
  private JPanel gPanel = new JPanel();
  private JPanel bPanel = new JPanel();
  private JPanel resultPanel = new JPanel();

  private JSlider rColor;
  private JSlider gColor;
  private JSlider bColor;
  private int r, g, b;

  @Override
  public void init() {

    rPanel.add(new JLabel("R:"));
    rPanel.add(rColor = new JSlider(0, 255));
    gPanel.add(new JLabel("G:"));
    gPanel.add(gColor = new JSlider(0, 255));
    bPanel.add(new JLabel("B:"));
    bPanel.add(bColor = new JSlider(0, 255));
    resultPanel.setPreferredSize(new Dimension(300, 300));
    JPanel mainPanel = new JPanel();
    mainPanel.add(rPanel);
    mainPanel.add(gPanel);
    mainPanel.add(bPanel);
    mainPanel.add(resultPanel);

    resultPanel.setBackground(Color.black);
    this.getContentPane().add(mainPanel);

    rColor.addChangeListener(this);
    gColor.addChangeListener(this);
    bColor.addChangeListener(this);
    rColor.setValue(100);
    gColor.setValue(100);
    bColor.setValue(100);
  }

  public static void main(String[] args) {
    RgbColorSelect r = new RgbColorSelect();
    r.display();
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource().equals(rColor)) {
      r = rColor.getValue();
    } else if (e.getSource().equals(gColor)) {
      g = gColor.getValue();
    } else if (e.getSource().equals(bColor)) {
      b = bColor.getValue();
    }
    Color c = new Color(r, g, b);
    resultPanel.setBackground(c);
    resultPanel.setToolTipText(c.toString());
    resultPanel.setBorder(BorderFactory.createTitledBorder(c.toString()));
  }
}
