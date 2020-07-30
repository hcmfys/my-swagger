package org.springbus.j3d;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawRectFrame  extends  BasicFrame{

    public static void main(String[] args) {
        new DrawRectFrame().run(args);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(100, 100, 200, 100);

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform oldAt = g2.getTransform();

        AffineTransform at = new AffineTransform();

        at.setToRotation(Math.PI * 60 / 180,100,100);
        at.translate(100, 100);

        System.out.println(at.toString());

        g2.setColor(Color.PINK);
        g2.setTransform(at);
        g2.fillRect(0, 0, 200, 100);
        g2.setTransform(oldAt);

    }
}
