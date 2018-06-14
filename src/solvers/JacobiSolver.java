package solvers;

import model.Complex;
import model.Matrix;
import model.Vector;

import java.util.stream.IntStream;

public class JacobiSolver extends IterativeSolver {

    @Override
    public Vector solve(Matrix A, Vector b) {
        Matrix D = A.getDiagonalMatrix();
        Matrix D_inv = new Matrix();
        IntStream.range(0, D.shape()[0]).forEach(i -> D_inv.set(i, i, new Complex(1).div(D.get(i, i))));
        Matrix R = A.subtract(D);
        Vector x = new Vector();
        Vector x_old;
        reset();
        do {
            step();
            x_old = x.copy();
            x = D_inv.dot(b.subtract(R.dot(x_old)));
        } while (x.distanceTo(x_old) > getEps());
        return x;
    }
}
