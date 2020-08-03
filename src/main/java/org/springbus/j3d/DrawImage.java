package org.springbus.j3d;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawImage  extends  BasicFrame implements MouseListener {

    private  int a=5;
    public static void main(String[] args) {
        DrawImage af =new DrawImage();
        af.run(args);
        af.addMouseListener(af);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2=(Graphics2D)g;


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());
        try {
            BufferedImage bufferedImage= ImageIO.read(new File("/Volumes/d/pic/timg.jpeg"));
            g2.setPaint(new TexturePaint(bufferedImage,
                    new Rectangle(bufferedImage.getWidth()/2,bufferedImage.getHeight()/2 )));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //g2.setColor(Color.GRAY);
        g2.fillRect(100, 100, 200, 100);
        AffineTransform oldAt = g2.getTransform();

        AffineTransform at = new AffineTransform();

        at.setToRotation(Math.PI * 60 / 180,100,100);
        at.translate(100, 100);

        System.out.println(at.toString());
        // g2.setComposite(composite);

        //g2.setColor(Color.PINK);
        g2.setTransform(at);
        g2.fillRect(0, 0, 200, 100);
        g2.setTransform(oldAt);
        //g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(2f));
        // g2.setComposite(AlphaComposite.SrcAtop);
        g2.fillOval(100-150/2, 100-150/2, 150, 150);
        g2.setColor(Color.red);
        AffineTransform affineTransform=new AffineTransform();
        affineTransform.setToRotation((a+30) * Math.PI/ 180, 400, 120);
        affineTransform.translate(400,120);
        g2.setTransform(affineTransform);
        g2.drawString("关晓彤", 0, 0);
        g2.dispose();


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        a=a+5;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
