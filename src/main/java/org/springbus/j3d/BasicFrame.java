package org.springbus.j3d;

import javax.swing.*;
import java.awt.*;

public class BasicFrame extends  JFrame {

    private  void initUI(){
        this.setSize(600, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screen=  Toolkit.getDefaultToolkit().getScreenSize();
        int px=screen.width/2-this.getWidth()/2;
        int py=screen.height/2-this.getHeight()/2;
        this.setLocation(px, py);

    }

    public void run(String[] args) {
        initUI();
    }
}
