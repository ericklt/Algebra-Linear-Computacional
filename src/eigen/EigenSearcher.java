package eigen;

import Utils.Pair;
import model.Complex;
import model.Matrix;
import model.Vector;

public abstract class EigenSearcher {

    public double err(Complex newL, Complex oldL) {
        return oldL.distanceTo(newL);
//        return Math.abs((newL - oldL) / newL);
    }

    public Complex getEigenValue(Matrix A, Vector q) {
        return q.dot(A.dot(q)).div(q.dot(q));
    }

    public abstract Pair<Complex, Vector> findEigen(Matrix m);

}
