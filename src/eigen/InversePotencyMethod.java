package eigen;

import Utils.Pair;
import decomposition.LUDecomposition;
import model.Complex;
import model.Matrix;
import model.Vector;
import solvers.Solver;

public class InversePotencyMethod extends EigenSearcher {

    private double eps;
    private Vector start;

    public InversePotencyMethod(Vector start, double eps) {
        this.start = start;
        this.eps = eps;
    }

    @Override
    public Pair<Complex, Vector> findEigen(Matrix A) {
        Vector x = start;
        Complex L = new Complex(), oldL;
        Pair<Matrix, Matrix> LU = new LUDecomposition().decompose(A);
        do {
            oldL = L;
            x = Solver.solveByRetroSubstitution(LU._2(), Solver.solveBySubstitution(LU._1(), x)).normalized();
            L = getEigenValue(A, x);
            System.out.println("Old: " + oldL + " - Eigenvalue: " + L + ", err : " + err(L, oldL));
        } while(this.err(L, oldL) > eps);
        return new Pair<>(L, x);
    }
}
