package eigen;

import Utils.Pair;
import model.Matrix;
import model.Vector;

public class DislocatedPotencyMethod extends EigenSearcher {

    private double eps, mu;

    public DislocatedPotencyMethod(double eps, double mu) {
        this.eps = eps;
        this.mu = mu;
    }

    @Override
    public Pair<Double, Vector> findEigen(Matrix A, Vector x) {
        A = A.subtract(Matrix.identity(A.shape()[0]).dot(mu));
        Pair<Double, Vector> inverseResult = new InversePotencyMethod(eps).findEigen(A, x);
        return new Pair<>(inverseResult._1() + mu, inverseResult._2());
//        double L = 0, oldL;
//        Pair<Matrix, Matrix> LU = A.LUDecomposition();
//        do {
//            oldL = L;
//            x = LU._2().solveByRetroSubstitution(LU._1().solveBySubstitution(x));
//            L = getEigenValue(A, x.normalized());
//            System.out.println("Old: " + oldL + " - Eigenvalue: " + L + ", err : " + err(L, oldL));
//        } while(this.err(L, oldL) > eps);
//        return new Pair<>(L + mu, x.normalized());
    }

}
