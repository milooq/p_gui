package com.company;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;

public class Animator extends Thread{

    private double step = 0.1;
    private Graphics g;
    private static int scale;
    private int len;
    Polinom poly;
    BufferedImage image, frame;
    Graphics frameGraphics;

    private static int cX = Window.width/2;
    private static int cY = Window.height/2;

    public int index = 0;
    public double[] xes = new double[20];
    public double[] yes = new double[20];

    DrawFrameThread p;

    public void update_center(int x, int y){
        cX = x;
        cY = y;
    }

    public void setPoly(Polinom poly) {
        this.poly = poly;
    }

    public Animator(Graphics g, int scale, int len) {
        this.g = g;
        this.scale = scale;
        //this.poly = poly;
        this.len = len;
        frame = new BufferedImage(Window.width,Window.height,BufferedImage.TYPE_INT_RGB );
        frameGraphics = frame.getGraphics();

        try {
            image = ImageIO.read(new File("pencil.png"));
//            calcTransform();
        }catch(IOException e) {
            System.out.println("Пикчу не нашел(");
        }
        p = new DrawFrameThread();

        drawAxis();
    }

    //deprecated
    public void display_current_frame(Polinom poly, double xes[], double yes[],int index){
            clear(); //очистить экра
            drawPolynom(poly);
            drawAxis(); //нарисовать оси
            int wight = 4;

            frameGraphics.setColor(Color.red);
            for (int i = 0; i < index; i++) {
                frameGraphics.fillOval(Animator.untranslateX(xes[i]) - wight / 2, Animator.untranslateY(yes[i]) - wight / 2, wight, wight);
            }
            frameGraphics.setColor(Color.black);
    }

    public void changeScaleAndCenter(int sign, int x, int y){
        if(sign > 0){
            scale *= 1.0+step;
            double nx = 0 - (step)*translateX(x);
            double ny = 0 - (step)*translateY(y);
            update_center(untranslateX(nx), untranslateY(ny));
        }else{
            double nx = 0 - (1/(1.0+step) - 1)*translateX(x);
            double ny = 0 - (1/(1.0+step) - 1)*translateY(y);
            update_center(untranslateX(nx), untranslateY(ny));
            scale /= 1.0+step;
        }
    }

    public void drawAxis() {
        frameGraphics.setColor(Color.black);

        //рисуем 0 в центре
        frameGraphics.drawString("0", cX-len*3, cY-len);

        //ось игрек
        frameGraphics.drawLine(cX, 0, cX, Window.height);
        frameGraphics.drawString("Y", cX+10, 43);
        frameGraphics.drawLine(cX, 30, cX-10, 40);//стрелочка
        frameGraphics.drawLine(cX, 30, cX+10, 40);

        for (int i = 1; i < (int) (Window.height /( 2 * scale)); i++) {
            int disp = len;
            if(i > 9) disp = 2*len - 3;
            frameGraphics.drawString("" + i, cX - 3 *  disp, cY - scale * i);
            frameGraphics.drawString("-" + i, cX - 3 *  disp - 2 , cY + scale * i); // -2 потому что еще минус!
            frameGraphics.drawLine(cX - len, cY + scale * i, cX + len, cY+scale * i); // отметка
            frameGraphics.drawLine(cX - len, cY -scale * i, cX + len, cY - scale * i); // отметка
        }

        //ось икс
        frameGraphics.drawLine(0, cY, Window.width, cY);
        frameGraphics.drawString("X", Window.width-10, cY-11);
        frameGraphics.drawLine(Window.width - 10, cY-10, Window.width-5, cY);//стрееелочка
        frameGraphics.drawLine(Window.width-10, cY+10, Window.width-5, cY);

        for (int i = 1; i < (Window.width /(2 * scale)); i++) {
            frameGraphics.drawString("" + i, cX + scale * i - 2 * len, cY - len - 2);
            frameGraphics.drawString("-" + i, cX - scale * i - 2 * len, cY - len - 2 );
            frameGraphics.drawLine(cX - scale * i, cY - len, cX -scale * i, cY + len); // отметка
            frameGraphics.drawLine(cX + scale*i, cY-len, cX + scale*i, cY+len); // отметка
        }
    }

    public void clear(){
        frameGraphics.setColor(Color.white);
        frameGraphics.fillRect(0,0,Window.width,Window.height);
        frameGraphics.setColor(Color.black);
    }

    static public double translateX (int x){
        return (x - cX)* 1.0/scale;
    }

    static public double translateY (int y){
        return -(y-cY)*1.0/scale;
    }

    static public int untranslateX (double x){
        return (int)((x *scale) + cX);
    }

    static public int untranslateY (double y){
        return (int)(-y*scale+cY);
    }

    @Override
    public void run() {
        while (true){
//            clear(); //очистить экран
//            if(poly!=null){
//            drawPolynom(poly);
//            }
//            drawAxis(); //нарисовать оси
//            drawPoints();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            g.drawImage(frame,0,0, Window.width,Window.height,null);

        }


    }

    public void drawPolynom(Polinom poly) {
        int interval = Window.width / (2 * scale);
        double precision = 1e-1;
        double prev_x = -interval;
        double prev_y = poly.getValue(-interval);
        for (double i = -interval; i < interval; i += precision) {
            double curr_x = i;
            double curr_y = poly.getValue(i);
            frameGraphics.drawLine(untranslateX(prev_x), untranslateY(prev_y),
                    untranslateX(curr_x), untranslateY(curr_y));
//            clearPencil(prev_x, prev_y);
//            drawPencil(curr_x, curr_y);
            prev_x = curr_x;
            prev_y = curr_y;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    class DrawFrameThread implements Runnable {
        public void run() {
            clear(); //очистить
            drawAxis(); //нарисовать оси
            drawPoints();
            if(poly!=null){
                drawPolynom(poly);
            }
        }
    }

    public void drawFrame(){
        new Thread(p).start();
    }

    public void drawPoints(){
        frameGraphics.setColor(Color.red);
        int wight = 4;
        for(int i=0; i < index; i++){
            frameGraphics.fillOval(untranslateX(xes[i]) - wight/2, untranslateY(yes[i])-wight/2, wight ,wight);
        }
        frameGraphics.setColor(Color.black);
    }

    public void addPoint(double x, double y){
        xes[index] = x;
        yes[index] = y;
        index++;
    }

    public void changeBufferedImageSize(){
        frame = new BufferedImage(Window.width,Window.height,BufferedImage.TYPE_INT_RGB );
        frameGraphics = frame.getGraphics();
    }

    final double point_ratio = 0.58854860186; // отношение размера картинки по x к координате вершини карандаша
    final double dx_dy_ratio = 0.74282888229; // отношение ширины к высоте
    int dy = 100;
    int dx = (int)(dy*dx_dy_ratio);

    public void drawPencil(double x_co, double y_co){
        frameGraphics.drawImage(image , untranslateX(x_co)-(int)(point_ratio*dx), untranslateY(y_co)-dy,dx, dy, null);
    }

    public void clearPencil(double x_co, double y_co){
        frameGraphics.setColor(Color.white);
        frameGraphics.fillRect(untranslateX(x_co)-(int)(point_ratio*dx), untranslateY(y_co)-dy,dx, dy);
        frameGraphics.setColor(Color.black);
    }

}
