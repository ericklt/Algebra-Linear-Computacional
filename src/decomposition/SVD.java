package decomposition;

import Utils.MyMath;
import Utils.Pair;
import Utils.Triple;
import eigen.TransformationMethod;
import model.Complex;
import model.Matrix;
import model.Vector;

import java.util.List;
import java.util.stream.IntStream;

public class SVD {

    private SVD(){}

    public static Triple<Matrix, Matrix, Matrix> decompose(Matrix A) {
        boolean inverted = A.shape()[1] > A.shape()[0];
        if (inverted) A = A.T();
        List<Pair<Complex, Vector>> eigens = TransformationMethod.findAllEigens(A.T().dot(A));
        Matrix V = new Matrix();
        Matrix lambda = new Matrix();
        eigens.forEach(pair -> {
            int n = lambda.size();
            lambda.set(n, n, MyMath.sqrt(pair._1().getReal()));
            V.addColumn(pair._2());
        });
        Matrix U = new Matrix();
        Matrix AV = A.dot(V);
        IntStream.range(0, AV.shape()[1]).forEach(i -> U.addColumn(AV.getColumn(i).dot(1 / lambda.get(i, i).getReal())));

        return inverted ? new Triple<>(V, lambda, U.T()) : new Triple<>(U, lambda, V.T());
    }

}
