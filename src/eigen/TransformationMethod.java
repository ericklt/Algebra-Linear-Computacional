package eigen;

import Utils.Pair;
import decomposition.Decomposition;
import decomposition.HouseHolderDecomposition;
import decomposition.JacobDecomposition;
import model.Complex;
import model.Matrix;
import model.Vector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransformationMethod {

    private static Vector getEigenVector(Matrix A, int i) {
        Vector v = new Vector();
        for (int j = i-1; j >= 1; j--)
            v.set(j, A.get(j, i).sum(A.get(j).dot(v)).mul(-1).div(A.get(j, j).sub(A.get(i, i))));
        v.set(i, new Complex(1));
        return v;
    }

    public static List<Pair<Complex, Vector>> findAllEigens(Matrix m) {
        int n = m.size();
        Matrix A = m.copy();
        Matrix H = Matrix.identity(n);

        for (int j = 0; j < n-2; j++) {
            Matrix Hj = HouseHolderDecomposition.createHj(A, j+1, j);
            H = H.dot(Hj);
            A = Hj.dot(A.dot(Hj));
        }

        Matrix X = H;
        Decomposition decomposition = new JacobDecomposition();

        int it = 0;
        while (it < 10000 && A.errorBelowDiagonal() > 0.0001) {
            it++;
            Pair<Matrix, Matrix> QR = decomposition.decompose(A);
            A = QR._2().dot(QR._1());
            X = X.dot(QR._1());
        }

        final Matrix Xfinal = X;
        final Matrix Afinal = A.copy();

        return IntStream.range(0, n)
                .mapToObj(i -> new Pair<>(Afinal.get(i, i), Xfinal.dot(getEigenVector(Afinal, i))))
                .collect(Collectors.toList());

    }
}
