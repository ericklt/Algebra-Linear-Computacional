package model;

import Utils.Pair;

import java.text.DecimalFormat;

public class Complex extends Pair<Double, Double> {

    public Complex() {
        this(0);
    }

    public Complex(double n) {
        this(n, 0);
    }

    public Complex(double real, double im) {
        super(real, im);
    }

    public double getReal() {
        return this._1();
    }

    public double getIm() {
        return this._2();
    }

    public Complex conjugate() {
        return new Complex(this.getReal(), -this.getIm());
    }

    public Complex sum(double x) {
        return new Complex(this.getReal() + x, this.getIm());
    }

    public Complex sum(Complex c) {
        return new Complex(this.getReal() + c.getReal(), this.getIm() + c.getIm());
    }

    public Complex sub(double x) {
        return new Complex(this.getReal() - x, this.getIm());
    }

    public Complex sub(Complex c) {
        return new Complex(this.getReal() - c.getReal(), this.getIm() - c.getIm());
    }

    public Complex mul(double x) {
        return new Complex(this.getReal() * x, this.getIm() * x);
    }

    public Complex mul(Complex c) {
        double r1 = this.getReal(), i1 = this.getIm();
        double r2 = c.getReal(), i2 = c.getIm();
        return new Complex(r1 * r2 - i1 * i2, i1 * r2 + r1 * i2);
    }

    public double mulByConjugate() {
        return this.mul(this.conjugate()).getReal();
    }

    public Complex div(double x) {
        return this.mul(1/x);
    }

    public Complex div(Complex c) {
        return this.mul(c.conjugate()).div(c.mulByConjugate());
    }

    public double size() {
        return Math.hypot(getReal(), getIm());
    }

    public double distanceTo(Complex c) {
        return (c.sub(this)).size();
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("0.00");
        String realS = format.format(getReal());
        String imS = format.format(getIm()) + "i";
        if (Math.abs(getIm()) < 0.00001)
            return realS;
        else if (Math.abs(getReal()) < 0.00001)
            return imS;
        else if (getIm() > 0)
            return realS + "+" + imS;
        else
            return realS + imS;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Complex)) return false;
        return this.distanceTo((Complex) o) < 0.0001;
    }
}
