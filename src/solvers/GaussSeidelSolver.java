package solvers;

import model.Complex;
import model.Matrix;
import model.Vector;

public class GaussSeidelSolver extends IterativeSolver {

    @Override
    public Vector solve(Matrix A, Vector b) {
        Vector x = new Vector();
        Vector x_old;
        reset();
        do {
            step();
            x_old = x.copy();
            for (int i = 0; i < A.shape()[0]; i++) {
                Complex acc1 = new Complex();
                Complex acc2 = new Complex();
                for (int j = 0; j < A.shape()[0]; j++) {
                    if (j < i)
                        acc1 = acc1.sum(A.get(i, j).mul(x.get(j)));
                    if (j > i)
                        acc2 = acc2.sum(A.get(i, j).mul(x.get(j)));
                }
                x.set(i, b.get(i).sub(acc1).sub(acc2).div(A.get(i, i)));
            }
        } while (x.distanceTo(x_old) > getEps());
        return x;
    }

}
