package com.company;

import java.util.ArrayList;
public class Polinom {
    //private ArrayList<Double> array;
    private double array[];
    private int deg;

    public int getDeg() {
        return array.length - 1;
    }

    public Polinom(double[] array) {
        this.array = new double[array.length];
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = array[array.length - 1 - i];
        }
        this.deg = getDeg();
    }

    private Polinom(int deg) {
        this.deg = deg;
        this.array = new double[this.deg + 1];
    }

    public Polinom(Polinom p) {
        this.deg = p.deg;
        this.array = new double[this.deg + 1];
        System.arraycopy(p.array, 0, this.array, 0, p.deg + 1);
    }

    public static Polinom sum(Polinom a, Polinom b) {
        int max_deg = Math.max(a.deg, b.deg);
        int min_deg = Math.min(a.deg, b.deg);
        Polinom res = new Polinom(max_deg);
        for (int i = 0; i <= min_deg; i++) {
            res.array[i] = a.array[i] + b.array[i];
        }
        if (a.deg > b.deg) System.arraycopy(a.array, min_deg + 1, res.array, min_deg + 1, max_deg - min_deg);
        else if (b.deg > a.deg) System.arraycopy(b.array, min_deg + 1, res.array, min_deg + 1, max_deg - min_deg);
        res.cheak();
        return res;
    }

    public static Polinom sub(Polinom a, Polinom b) {
        int max_deg = Math.max(a.deg, b.deg);
        int min_deg = Math.min(a.deg, b.deg);
        Polinom res = new Polinom(max_deg); //новый полином размера большего из складываемых полиномов
        for (int i = 0; i <= min_deg; i++) {
            res.array[i] = a.array[i] - b.array[i];
        }
        if (a.deg > b.deg) System.arraycopy(a.array, min_deg + 1, res.array, min_deg + 1, max_deg - min_deg);
        else if (b.deg > a.deg) System.arraycopy(b.array, min_deg + 1, res.array, min_deg + 1, max_deg - min_deg);
        res.cheak();
        return res;
    }

    private void cheak() {//trim
        if (this.deg == 0) return;
        int i;
        for (i = this.deg; i >= 0; i--) {
            if (this.array[i] != 0) break;
        }
        if (i == this.deg || i == -1) return;
        double newArray[] = new double[this.deg + 1];
        System.arraycopy(this.array, 0, newArray, 0, this.deg + 1);
        this.deg = i;
        this.array = new double[this.deg + 1];
        System.arraycopy(newArray, 0, this.array, 0, this.deg + 1);
        return;
    }

    public static Polinom multiplication(Polinom a, Polinom b) {
        Polinom res = new Polinom(a.deg + b.deg);
        for (int i = a.deg; i >= 0; i--) {
            for (int j = b.deg; j >= 0; j--) {
                res.array[i + j] += a.array[i] * b.array[j];
            }
        }
        res.cheak();
        return res;
    }

    public double getValue(double x) {
        double res = array[0];
        double px = x;
        for (int i = 1; i < array.length; i++) {
            res += px * array[i];
            px *= x;
        }
        return res;
    }

    //сумма, умножение на число через произведение полиномов, разность полиномов , красивый вывод на экран

    public static Polinom mylty(double a, Polinom b) {
        double[] r = new double[1];
        r[0] = a;
        Polinom a1 = new Polinom(r);
        return multiplication(a1, b);
    }

    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (int i = this.deg; i >= 0; i--) {
            if (this.array[i] == 0) continue;

            if ((this.array[i] > 0) && (i != this.deg))
                str.append("+");
            if (this.array[i] == 1)
                if (i == 0) str.append(1);
                else if (i == 1) str.append("x");
                else str.append("x^" + i);
            else {
                if (this.array[i] == -1)
                    if (i == 0) str.append(-1);
                    else if (i == 1) str.append("-x");
                    else str.append("-x^" + i);
                else {
                    if ((int) this.array[i] == this.array[i]) str.append((int) this.array[i]);
                    else str.append(this.array[i]);
                    if (i == 1) str.append("x");
                    else if (i != 0) str.append("x^" + i);
                }
            }
        }
        return str.toString();
    }
}

