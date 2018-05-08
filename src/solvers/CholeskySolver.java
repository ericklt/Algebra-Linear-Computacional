package solvers;

import decomposition.CholeskyDecomposition;
import decomposition.Decomposition;
import model.Matrix;
import model.Vector;

public class CholeskySolver extends Solver {

    private Decomposition decomposition = new CholeskyDecomposition();

    @Override
    public Vector solve(Matrix A, Vector b) {
        Matrix cho = decomposition.decompose(A)._1();
        return solveByRetroSubstitution(cho.T(), solveBySubstitution(cho, b));
    }
}
