package org.springbus.j3d;

import javax.swing.*;

public class BasicFrame extends  JFrame {

    private  void initUI(){
        this.setSize(600, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void run(String[] args) {
        initUI();
    }
}
