package eigen;

import Utils.Pair;
import model.Complex;
import model.Matrix;
import model.Vector;

public class DislocatedPotencyMethod extends EigenSearcher {

    private double eps, mu;
    private Vector start;

    public DislocatedPotencyMethod(Vector start, double eps, double mu) {
        this.start = start;
        this.eps = eps;
        this.mu = mu;
    }

    @Override
    public Pair<Complex, Vector> findEigen(Matrix A) {
        A = A.subtract(Matrix.identity(A.shape()[0]).dot(mu));
        Pair<Complex, Vector> inverseResult = new InversePotencyMethod(start, eps).findEigen(A);
        return new Pair<>(inverseResult._1().sum(mu), inverseResult._2());
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
