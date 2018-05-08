package solvers;

import model.Complex;
import model.Matrix;
import model.PivotingMode;
import model.Vector;

public class GaussianEliminationSolver extends Solver {

    private PivotingMode mode;

    public GaussianEliminationSolver() {
        this(PivotingMode.NONE);
    }

    public GaussianEliminationSolver(PivotingMode mode) {
        this.mode = mode;
    }

    public void setMode(PivotingMode mode) {
        this.mode = mode;
    }

    public static boolean partialPivoting(Matrix A, int k) {
        return partialPivoting(A, k, new Vector());
    }

    private static boolean partialPivoting(Matrix A, int k, Vector b) {
        int greatestRow = -1;
        Complex greatest = A.get(k, k);
        for (int i = k+1; i < A.shape()[0]; i++) {
            if (A.get(i, k).size() > greatest.size()) {
                greatestRow = i;
                greatest = A.get(i, k);
            }
        }
        if (greatestRow == -1) return false;

        A.swapRows(k, greatestRow);
        b.swap(k, greatestRow);
        return true;
    }


    private static boolean totalPivoting(Matrix A, int k, Vector b, Matrix columnPermutation) {
        int greatestRow = -1;
        int greatestCol = -1;
        Complex greatest = A.get(k, k);
        for (int i = k; i < A.shape()[0]; i++) {
            for (int j = k; j < A.shape()[1]; j++) {
                if (A.get(i, j).size() > greatest.size()) {
                    greatestRow = i;
                    greatestCol = j;
                    greatest = A.get(i, j);
                }
            }
        }
        if (greatestRow == -1) return false;

        A.swapRows(k, greatestRow);
        b.swap(k, greatestRow);
        A.swapColumns(k, greatestCol);
        columnPermutation.swapRows(k, greatestCol);
        return true;
    }

    @Override
    public Vector solve(Matrix A, Vector b) {
        if (A.shape()[0] != A.shape()[1]) {
            System.err.println("It is not a square matrix!");
            return null;
        }
        Matrix copy = A.copy();
        b = b.copy();
        Matrix colPermutation = Matrix.identity(copy.shape()[0]);
        for (int k = 0; k < copy.size()-1; k++) {
            if (mode == PivotingMode.PARTIAL)
               partialPivoting(copy, k, b);
            else if (mode == PivotingMode.TOTAL)
                totalPivoting(copy, k, b, colPermutation);

            for (int i = k+1; i < copy.size(); i++) {
                if (copy.get(k, k).equals(new Complex())) {
                    System.err.println("Some pivot is 0, aborting gauss!");
                    return null;
                }
                Complex multiplier = copy.get(i, k).div(copy.get(k, k)).mul(-1);
                copy.set(i, copy.get(i).sum(copy.get(k).dot(multiplier)));
                b.set(i, b.get(i).sum(b.get(k).mul(multiplier)));
            }
        }
        return Solver.solveByRetroSubstitution(copy, b, colPermutation);
    }

}
