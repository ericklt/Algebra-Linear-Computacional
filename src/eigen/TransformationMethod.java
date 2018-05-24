package eigen;

import Utils.Pair;
import decomposition.Decomposition;
import decomposition.HouseHolderDecomposition;
import decomposition.JacobDecomposition;
import model.Complex;
import model.Matrix;
import model.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransformationMethod {

    private static Vector getEigenVector(Matrix A, int i) {
        Vector v = new Vector();
        for (int j = i-1; j >= 0; j--)
            v.set(j, A.get(j, i).sum(A.get(j).dot(v)).mul(-1).div(A.get(j, j).sub(A.get(i, i))));
        v.set(i, new Complex(1));
        return v;
    }

    private static Matrix inverseOf2by2(Matrix A) {
        if (A.shape()[0] == 1 && A.shape()[1] == 1)
            return new Matrix(new Vector(Collections.singletonList(new Complex(1).div(A.get(0, 0)))));
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

    private static Vector blockVector2Vector(BlockMatrix blockVector) {
        Vector v = new Vector();
        for (int i = 0; i < blockVector.shape()[0]; i++)
            v.addAll(blockVector.get(i, 0).getColumn(0));
        return v;
    }

    private static Complex eigenValueOf2x2(Matrix A) {
        if (A.shape()[0] == 1 && A.shape()[1] == 1)
            return A.get(0, 0);
        if (A.shape()[0] != 2 || A.shape()[1] != 2) {
            System.err.println("Matrix is not 2x2");
            return null;
        }
        double b = A.trace().getReal();
        double c = A.determinant().getReal();
        Complex eVal = new Complex(b/2, Math.sqrt(4*c - b*b)/2);
        return eVal;
    }

    private static Matrix eigenVectorOf2x2(Matrix A, Complex eigenValue) {
        if (A.shape()[0] == 1 && A.shape()[1] == 1)
            return Matrix.parse("[[1]]");
        if (A.shape()[0] != 2 || A.shape()[1] != 2) {
            System.err.println("Matrix is not 2x2");
            return null;
        }
        Matrix m = new Matrix();
        m.addRow(new Vector(Collections.singletonList(A.get(0,1).div(eigenValue.sub(A.get(0, 0))))));
        m.addRow(Vector.parse("1"));
        return m;
    }

    private static List<Pair<Complex, Vector>> getAllEigenWithBlock(Matrix A, Matrix X) {
        List<Pair<Complex, Vector>> vectorList = new ArrayList<>();
        BlockMatrix blockMatrix = BlockMatrix.blockify(A);
        for (int I = 0; I < blockMatrix.shape()[0]; I++) {
            Complex eigenValue = eigenValueOf2x2(blockMatrix.get(I, I));
            BlockMatrix blockVector = new BlockMatrix();
            for (int J = I-1; J >= 0; J--) {
                Matrix inverse = inverseOf2by2(blockMatrix.get(J, J).subtract(Matrix.identity(2).dot(eigenValue)));
                Matrix result = new Matrix();
                for (int K = J+1; K < I; K++)
                    result.sum(blockMatrix.get(J, K).dot(blockVector.get(K, 0)));
                result = inverse.dot(result.sum(blockMatrix.get(J, I)).dot(-1));
                blockVector.addLine(Collections.singletonList(result));
            }
            Boolean is2x2Block = blockMatrix.getBlockSequence().get(I);
            blockVector.addLine(Collections.singletonList(eigenVectorOf2x2(blockMatrix.get(I, I), eigenValue)));
            Vector eigenVector = blockVector2Vector(blockVector);
            vectorList.add(new Pair<>(eigenValue, X.dot(eigenVector)));
            if (is2x2Block)
                vectorList.add(new Pair<>(eigenValue.conjugate(), X.dot(eigenVector.conjugate())));
        }
        return vectorList;
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

        return A.errorBelowDiagonal() < eps ?
                IntStream.range(0, n).mapToObj(i -> new Pair<>(Af.get(i, i), Xf.dot(getEigenVector(Af, i)).lastElementTo1())).collect(Collectors.toList()) :
                getAllEigenWithBlock(A, Xf).stream().map(p -> new Pair<>(p._1(), p._2().lastElementTo1())).collect(Collectors.toList());
    }
}
