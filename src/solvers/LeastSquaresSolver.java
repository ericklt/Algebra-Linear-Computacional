package solvers;

import model.Matrix;
import model.PivotingMode;
import model.Vector;

public class LeastSquaresSolver extends Solver {

    private Solver solver = new GaussianEliminationSolver(PivotingMode.TOTAL);

    @Override
    public Vector solve(Matrix A, Vector b) {
        Matrix aT = A.T();
        return solver.solve(aT.dot(A), aT.dot(b));
    }
}
