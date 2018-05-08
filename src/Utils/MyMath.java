package Utils;

import model.Complex;

public class MyMath {

    private MyMath(){}

    public static Complex sqrt(double x) {
        if (x < 0)
            return new Complex(0, Math.sqrt(-x));
        else
            return new Complex(Math.sqrt(x));
    }

}
