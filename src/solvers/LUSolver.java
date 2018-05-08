package solvers;

import Utils.Pair;
import decomposition.Decomposition;
import decomposition.LUDecomposition;
import model.Matrix;
import model.Vector;

public class LUSolver extends Solver {

    private Decomposition decomposition = new LUDecomposition();

    @Override
    public Vector solve(Matrix A, Vector b) {
        Pair<Matrix, Matrix> LU = decomposition.decompose(A);
        return solveByRetroSubstitution(LU._2(), solveBySubstitution(LU._1(), b));
    }
}
