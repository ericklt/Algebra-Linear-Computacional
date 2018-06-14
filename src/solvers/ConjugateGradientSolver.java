package solvers;

import model.Complex;
import model.Matrix;
import model.Vector;

public class ConjugateGradientSolver extends IterativeSolver {

    @Override
    public Vector solve(Matrix A, Vector b) {
        b = A.T().dot(b);
        A = A.T().dot(A);
        Vector x = new Vector();
        Vector r = b.subtract(A.dot(x));
        Vector p = r.copy();
        double rsold = r.sumSquared();
        reset();
        while(true) {
            step();
            Vector Ap = A.dot(p);
            Complex alpha = new Complex(rsold).div(p.conjugate().dot(Ap));
            x = x.sum(p.dot(alpha));

            r = r.subtract(Ap.dot(alpha));
            double rsnew = r.sumSquared();
            if (Math.sqrt(rsnew) < getEps()) break;
            p = r.sum(p.dot(rsnew / rsold));
            rsold = rsnew;
        }
        return x;
    }

}
