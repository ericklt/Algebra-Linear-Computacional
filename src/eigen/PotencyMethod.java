package eigen;

import Utils.Pair;
import model.Matrix;
import model.Vector;

public class PotencyMethod extends EigenSearcher {

    private double eps;

    public PotencyMethod(double eps) {
        this.eps = eps;
    }

    @Override
    public Pair<Double, Vector> findEigen(Matrix A, Vector x) {
        double L = 0, oldL;
        do {
            oldL = L;
            x = A.dot(x.normalized());
            L = getEigenValue(A, x.normalized());
            System.out.println("Old: " + oldL + " - Eigenvalue: " + L + ", err : " + err(L, oldL));
        } while(this.err(L, oldL) > eps);
        return new Pair<>(L, x.normalized());
    }
}
