package eigen;

import Utils.Pair;
import model.Complex;
import model.Matrix;
import model.Vector;

public class PotencyMethod extends EigenSearcher {

    private double eps;
    private Vector start;

    public PotencyMethod(Vector start, double eps) {
        this.start = start;
        this.eps = eps;
    }

    @Override
    public Pair<Complex, Vector> findEigen(Matrix A) {
        Vector x = start;
        Complex L = new Complex(), oldL;
        do {
            oldL = L;
            x = A.dot(x.normalized());
            L = getEigenValue(A, x.normalized());
            System.out.println("Old: " + oldL + " - Eigenvalue: " + L + ", err : " + err(L, oldL));
        } while(this.err(L, oldL) > eps);
        return new Pair<>(L, x.normalized());
    }
}
