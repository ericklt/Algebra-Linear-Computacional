package eigen;

import Utils.Pair;
import model.Matrix;
import model.Vector;

public class InversePotencyMethod extends EigenSearcher {

    private double eps;

    public InversePotencyMethod(double eps) {
        this.eps = eps;
    }

    @Override
    public Pair<Double, Vector> findEigen(Matrix A, Vector x) {
        double L = 0, oldL;
        Pair<Matrix, Matrix> LU = A.LUDecomposition();
        do {
            oldL = L;
            x = LU._2().solveByRetroSubstitution(LU._1().solveBySubstitution(x)).normalized();
            L = getEigenValue(A, x);
            System.out.println("Old: " + oldL + " - Eigenvalue: " + L + ", err : " + err(L, oldL));
        } while(this.err(L, oldL) > eps);
        return new Pair<>(L, x);
    }
}
