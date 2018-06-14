package decomposition;

import Utils.MyMath;
import Utils.Pair;
import decomposition.Decomposition;
import model.Complex;
import model.Matrix;

import java.util.stream.IntStream;

public class JacobiDecomposition implements Decomposition {

    private final double eps = 0.00001;

    public static Matrix createJijT(Matrix A, int i, int j) {
        double theta = A.get(j, j).equals(new Complex()) ? Math.PI/2 : Math.atan(A.get(i, j).div(A.get(j, j)).getReal());
        Matrix result = Matrix.identity(A.shape()[0]);
        result.set(i, i, new Complex(Math.cos(theta)));
        result.set(j, j, new Complex(Math.cos(theta)));
        result.set(i, j, new Complex(-Math.sin(theta)));
        result.set(j, i, new Complex(Math.sin(theta)));
        return result;
    }

    @Override
    public Pair<Matrix, Matrix> decompose(Matrix m) {
        Matrix A = m.copy();
        Matrix Qt = Matrix.identity(m.shape()[0]);
        do {
            for (int j = 0; j < m.shape()[1]; j++) {
                for (int i = j+1; i < m.shape()[0]; i++) {
                    Matrix JijT = createJijT(A, i, j);
                    A = JijT.dot(A);
                    Qt = JijT.dot(Qt);
                }
            }
        } while (A.errorBelowDiagonal() > eps);

        return new Pair<>(Qt.subRows(0, m.shape()[1]).T(), A.subRows(0, m.shape()[1]));
    }
}
