package com.company;

import jdk.jshell.spi.ExecutionControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


public class Window extends JFrame{

    public static int width = 900;
    public static int height = 900;
    public static int scale = 30;
    public static int len = 5;
    int index = 0;
    double[] xes = new double[20];
    double[] yes = new double[20];

    double x0 = 0, y0 = 0, x1 = 0, y1 = 0;

    Polinomial p;

    Animator a;

    public Window() {

        this.setVisible(true);
        this.setTitle("Polynomial Graphics");
        this.setSize(300,200);
//        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(500,100, width, height);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
//      add panel to main frame

        Graphics g = this.getGraphics();

        a = new Animator(g, scale, len);

        this.addComponentListener(new ComponentAdapter()   {
            @Override
            public void componentResized(ComponentEvent e) {
//              System.out.println("Resized to " + e.getComponent().getSize().getWidth());
                width = (int)e.getComponent().getSize().getWidth();

//               System.out.println("Resized to " + e.getComponent().getSize().getHeight());
                height = (int)e.getComponent().getSize().getHeight();

                a.update_center(width/2, height/2);
            }
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                a.changeScaleAndCenter(-e.getWheelRotation(), e.getX(), e.getY());
                a.display_current_frame(p.poly, xes, yes, index);
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x = Animator.translateX(e.getX());
                double y = Animator.translateY(e.getY());
                boolean flag = true;
                for(int i = 0; i<index; i++){
                    if(x == xes[i]){
                        JOptionPane.showMessageDialog(null, "Молодец, ты только что разделил на нуль");
                        flag = false;
                        continue;
                    }
                }
                if(flag) {
                    xes[index] = x;
                    yes[index] = y;
                    index++;
                }

                a.clear(); //очистить экран
                p = new Polinomial(Arrays.copyOfRange(xes, 0, index),Arrays.copyOfRange(yes, 0, index));
                a.drawPolynom(p.poly);
                a.drawAxis(); //нарисовать оси
                int wight = 4;

                System.out.printf("x = %.2f | y = %.2f\n",x, y);
                g.setColor(Color.red);
                for(int i=0; i < index; i++){
                    g.fillOval(Animator.untranslateX(xes[i]) - wight/2, Animator.untranslateY(yes[i])-wight/2, wight ,wight);
                }
                g.setColor(Color.black);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x0 = Animator.translateX(e.getX());
                y0 = Animator.translateY(e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //мб не работает
                //хз надо ли транслейтить
                //пусть кто-нибудь другой подумает
                x1 = Animator.translateX(e.getX());
                y1 = Animator.translateY(e.getY());
                double dx = x1 - x0;
                double dy = y1 - y0;
                a.update_center(Animator.untranslateX(dx), Animator.untranslateY(dy));
                if(p != null){
                    a.display_current_frame(p.poly, xes, yes, index);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(a != null) {
            a.clear();
            a.drawAxis();
            if(p != null) {
                a.display_current_frame(p.poly, xes, yes, index);
            }
        }
    }
}


class Polinomial extends JPanel {
    Polinom poly;

    public Polinomial(double[] x, double[] y) {
        Polinom[] l = new Polinom[x.length];

        for (int i = 0; i < l.length; i++) {
            l[i] = new Polinom(new double[]{1});
            for (int j = 0; j < l.length; j++) {
                if (i != j) {
                    l[i] = Polinom.mylty(
                            1 / (x[i] - x[j]), l[i]
                    );
                    l[i] = Polinom.multiplication(
                            l[i], new Polinom(new double[]{1.0, -1 * x[j]})
                    );
                }
            }
        }

        Polinom L = new Polinom(new double[0]);

        for (int t = 0; t < l.length; t++) {
            L = Polinom.sum(L, Polinom.mylty(y[t], l[t]));
        }
        this.poly = L;
    }
}