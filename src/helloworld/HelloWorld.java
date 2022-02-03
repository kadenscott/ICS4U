package helloworld;

import javax.swing.*;
import java.awt.*;

/**
 * File Name:	HelloWorld
 * Programmer:	Your name here
 * Date:		Current date here
 * Description:	This program demonstrates the
 * use of graphics.
 */
public class HelloWorld extends JFrame {
    public HelloWorld() {
        super("Hello World!");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        JLabel pageLabel = new JLabel("The Wonderful Thing About Tiggers");
        FlowLayout flo = new FlowLayout();
        setLayout(flo);
        add(pageLabel);
        setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HelloWorld hw = new HelloWorld();
    }
}