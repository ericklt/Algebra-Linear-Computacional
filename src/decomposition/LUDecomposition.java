package decomposition;

import Utils.Pair;
import model.Complex;
import model.Matrix;

public class LUDecomposition implements Decomposition {
    @Override
    public Pair<Matrix, Matrix> decompose(Matrix m) {
        if (m.shape()[0] != m.shape()[1]) {
            System.err.println("It is not a square matrix!");
            return null;
        }
        Matrix L = Matrix.identity(m.size());
        Matrix U = m.copy();

        for (int k = 0; k < U.size()-1; k++) {
            for (int i = k+1; i < U.size(); i++) {
                if (U.get(k, k).equals(new Complex())) {
                    System.err.println("Some pivot is 0, aborting LU!");
                    return null;
                }
                Complex multiplier = U.get(i, k).div(U.get(k, k)).mul(-1);
                U.set(i, U.get(i).sum(U.get(k).dot(multiplier)));
                L.set(i, k, multiplier.mul(-1));
            }
        }
        return new Pair<>(L, U);
    }
}
