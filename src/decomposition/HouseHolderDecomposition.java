package decomposition;

import Utils.Pair;
import model.Matrix;
import model.Vector;

import java.util.stream.IntStream;

public class HouseHolderDecomposition implements Decomposition {

    public static Matrix createHj(Matrix aj, int j) {
        return createHj(aj, j, j);
    }

    public static Matrix createHj(Matrix aj, int i, int j) {
        int m = aj.shape()[0];
        Vector v = new Vector();
        IntStream.range(i, m).forEach(k -> v.set(k , aj.get(k, j)));
        double n_v = v.norm();
        Vector N = (v.subtract( Vector.base(m, i).dot((v.get(i).getReal() < 0)? n_v : -n_v) )).normalized();
        Matrix n = new Matrix();
        n.addColumn(N);
        return Matrix.identity(m).subtract(n.dot(n.T()).dot(2));
    }

    @Override
    public Pair<Matrix, Matrix> decompose(Matrix m) {
        Matrix H = Matrix.identity(m.shape()[0]);
        Matrix A = m.copy();
        for (int j = 0; j < m.shape()[1]; j++) {
            Matrix Hj = createHj(A, j);
            A = Hj.dot(A);
            H = Hj.dot(H);
        }
        return new Pair<>(H.subRows(0, m.shape()[1]).T(), A.subRows(0, m.shape()[1]));
    }
}
