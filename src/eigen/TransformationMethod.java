package eigen;

import Utils.Pair;
import decomposition.Decomposition;
import decomposition.HouseHolderDecomposition;
import decomposition.JacobDecomposition;
import model.Complex;
import model.Matrix;
import model.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransformationMethod {

    private static Matrix inverseOf2by2(Matrix A) {
        if (A.shape()[0] != 2 || A.shape()[1] != 2) {
            System.err.println("Matrix is not 2x2");
            return null;
        }
        Complex det = A.determinant();
        Matrix inverse = new Matrix();
        inverse.addRow(A.get(1, 1).div(det), A.get(0, 1).mul(-1).div(det));
        inverse.addRow(A.get(1, 0).mul(-1).div(det), A.get(0, 0).div(det));
        return inverse;
    }

    private static Vector getEigenVector(Matrix A, int i) {
        Vector v = new Vector();
        for (int j = i-1; j >= 1; j--)
            v.set(j, A.get(j, i).sum(A.get(j).dot(v)).mul(-1).div(A.get(j, j).sub(A.get(i, i))));
        v.set(i, new Complex(1));
        return v;
    }

    private static Vector getEigenVectorBlock(Matrix A, int i) {
        Vector v = new Vector();
        for (int j = i-1; j >= 1; j--)
            v.set(j, A.get(j, i).sum(A.get(j).dot(v)).mul(-1).div(A.get(j, j).sub(A.get(i, i))));
        v.set(i, new Complex(1));
        return v;
    }

    public static List<Pair<Complex, Vector>> findAllEigens(Matrix m) {
        final double eps = 0.0001;
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
        while (it < 10000 && A.errorBelowDiagonal() > eps) {
            it++;
            Pair<Matrix, Matrix> QR = decomposition.decompose(A);
            A = QR._2().dot(QR._1());
            X = X.dot(QR._1());
        }
        Matrix Af = A;
        Matrix Xf = X;

        if (A.errorBelowDiagonal() < eps) {
            return IntStream.range(0, n).mapToObj(i -> new Pair<>(Af.get(i, i), Xf.dot(getEigenVector(Af, i)))).collect(Collectors.toList());
        } else {
            System.out.println("Teeth found, giving only eigenvalues.");
            List<Pair<Complex, Vector>> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (i == n-1 || A.get(i+1, i).size() < eps) {
                    result.add(new Pair<>(A.get(i, i), new Vector()));
                } else {
                    Matrix block = A.subSquareMatrix(i, i+2);
                    double b = block.trace().getReal();
                    double c = block.determinant().getReal();
                    Complex lambda1 = new Complex(b/2, Math.sqrt(4*c - b*b)/2);
                    result.add(new Pair<>(lambda1, new Vector()));
                    result.add(new Pair<>(lambda1.conjugate(), new Vector()));
                    i++;
                }
            }
            return result;
        }
    }
}
