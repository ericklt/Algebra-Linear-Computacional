package solvers;

import model.Complex;
import model.Matrix;
import model.Vector;

public class SteepestDescentSolver extends IterativeSolver {

    @Override
    public Vector solve(Matrix A, Vector b) {
        b = A.T().dot(b);
        A = A.T().dot(A);
        Vector x = new Vector();
        reset();
        while(true) {
            step();
            Vector r = b.subtract(A.dot(x));
            Complex alpha = new Complex(r.sumSquared()).div(r.conjugate().dot(A.dot(r)));
            x = x.sum(r.dot(alpha));
            if (r.sumSquared() < getEps()) break;
        }
        return x;
    }

}
