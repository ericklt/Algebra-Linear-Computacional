package eigen;

import Utils.Pair;
import model.Matrix;
import model.Vector;

public abstract class EigenSearcher {

    public double err(double newL, double oldL) {
        return Math.abs(newL - oldL);
//        return Math.abs((newL - oldL) / newL);
    }

    public Double getEigenValue(Matrix A, Vector q) {
        return q.dot(A.dot(q)) / q.dot(q);
    }

    public abstract Pair<Double, Vector> findEigen(Matrix m, Vector start);

}
