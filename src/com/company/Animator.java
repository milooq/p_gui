package com.company;

import java.awt.*;
import java.util.Arrays;

public class Animator {

    private double step = 0.1;
    private Graphics g;
    private static int scale;
    private int len;

    private static int cX = Window.width/2;
    private static int cY = Window.height/2;

    public void update_center(int x, int y){
        cX = x;
        cY = y;
    }

    public Animator(Graphics g, int scale, int len) {
        this.g = g;
        this.scale = scale;
        this.len = len;
        drawAxis();
    }

    public void display_current_frame(Polinom poly, double xes[], double yes[],int index){
        clear(); //очистить экран
        drawPolynom(poly);
        drawAxis(); //нарисовать оси
        int wight = 4;

        g.setColor(Color.red);
        for(int i=0; i < index; i++){
            g.fillOval(Animator.untranslateX(xes[i]) - wight/2, Animator.untranslateY(yes[i])-wight/2, wight ,wight);
        }
        g.setColor(Color.black);
    }

    public void changeScaleAndCenter(int sign, int x, int y){
        clear();

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

//        this.scale += sign * step;
//        double ration = scale/old_scale;

//        System.out.println(cX);
//        System.out.println(cY);
        drawAxis();
    }
    public void drawAxis() {
        g.setColor(Color.black);

        //рисуем 0 в центре
        g.drawString("0", cX-len*3, cY-len);

        //ось игрек
        g.drawLine(cX, 0, cX, Window.height);
        g.drawString("Y", cX+10, 43);
        g.drawLine(cX, 30, cX-10, 40);//стрелочка
        g.drawLine(cX, 30, cX+10, 40);

        for (int i = 1; i < (int) (Window.height /( 2 * scale)); i++) {
            int disp = len;
            if(i > 9) disp = 2*len - 3;
            g.drawString("" + i, cX - 3 *  disp, cY - scale * i);
            g.drawString("-" + i, cX - 3 *  disp - 2 , cY + scale * i); // -2 потому что еще минус!
            g.drawLine(cX - len, cY + scale * i, cX + len, cY+scale * i); // отметка
            g.drawLine(cX - len, cY -scale * i, cX + len, cY - scale * i); // отметка
        }

        //ось икс
        g.drawLine(0, cY, Window.width, cY);
        g.drawString("X", Window.width-10, cY-11);
        g.drawLine(Window.width - 10, cY-10, Window.width-5, cY);//стрееелочка
        g.drawLine(Window.width-10, cY+10, Window.width-5, cY);

        for (int i = 1; i < (Window.width /(2 * scale)); i++) {
            g.drawString("" + i, cX + scale * i - 2 * len, cY - len - 2);
            g.drawString("-" + i, cX - scale * i - 2 * len, cY - len - 2 );
            g.drawLine(cX - scale * i, cY - len, cX -scale * i, cY + len); // отметка
            g.drawLine(cX + scale*i, cY-len, cX + scale*i, cY+len); // отметка
        }
    }
    public void clear(){
        g.setColor(Color.white);
        g.fillRect(0,0,Window.width,Window.height);
        g.setColor(Color.black);
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
    public void drawPolynom(Polinom poly) {
        int interval = Window.width /( 2 * scale );
        double precision = 1e-3;
        double prev_x = -interval;
        double prev_y = poly.getValue(-interval);
        for (double i = -interval; i < interval; i += precision) {
            double curr_x = i;
            double curr_y = poly.getValue(i);
            g.drawLine(untranslateX(prev_x), untranslateY(prev_y),
                    untranslateX(curr_x), untranslateY(curr_y));
            prev_x = curr_x;
            prev_y = curr_y;
        }
    }
}
