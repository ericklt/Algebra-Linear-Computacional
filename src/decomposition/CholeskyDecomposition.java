package decomposition;

import Utils.MyMath;
import Utils.Pair;
import model.Complex;
import model.Matrix;

public class CholeskyDecomposition implements Decomposition {
    @Override
    public Pair<Matrix, Matrix> decompose(Matrix m) {
        if (m.shape()[0] != m.shape()[1]) {
            System.err.println("It is not a square matrix!");
            return null;
        } else if (!m.equals(m.T())) {
            System.err.println("It is not a symmetric matrix!");
            return null;
        }
        Matrix result = new Matrix();
        for (int i = 0; i < m.shape()[0]; i++) {
            for (int j = 0; j <= i; j++) {
                Complex term = new Complex();
                for (int k = 0; k < j; k++)
                    term = term.sum( result.get(i, k).mul(result.get(j, k)) );
                term = m.get(i, j).sub( term );

                if (i == j) {
                    if (term.getReal() <= 0) {
                        System.err.println("Matrix is not positive definite");
                        return null;
                    } else {
                        result.set(i, j, MyMath.sqrt(term.getReal()));
                    }
                } else {
                    result.set(i, j, term.div(result.get(j, j)));
                }
            }
        }
        return new Pair<>(result, result.T());
    }
}
