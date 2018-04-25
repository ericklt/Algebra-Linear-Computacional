package decomposition;

import Utils.Pair;
import model.Matrix;

import java.util.stream.IntStream;

public class JacobDecomposition implements QRDecomposition {

    private final double eps = 0.00001;

    private double errorBelowDiagonal(Matrix A) {
        return Math.sqrt(
                IntStream.range(0, A.shape()[1])
                        .mapToDouble(j -> IntStream.range(j+1, A.shape()[0])
                                .mapToDouble(i -> A.get(i, j))
                                .map(x -> x*x)
                                .sum())
                        .map(x -> x*x)
                        .sum()
        );
    }

    private Matrix createJijT(Matrix A, int i, int j) {
        double theta = A.get(j, j) == 0 ? Math.PI/2 : Math.atan(A.get(i, j) / A.get(j, j));
        Matrix result = Matrix.identity(A.shape()[0]);
        result.set(i, i, Math.cos(theta));
        result.set(j, j, Math.cos(theta));
        result.set(i, j, -Math.sin(theta));
        result.set(j, i, Math.sin(theta));
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
        } while (errorBelowDiagonal(A) > eps);

        return new Pair<>(Qt.subRows(0, m.shape()[1]).T(), A.subRows(0, m.shape()[1]));
    }
}
