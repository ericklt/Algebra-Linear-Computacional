package decomposition;

import Utils.MyMath;
import Utils.Pair;
import Utils.Triple;
import eigen.TransformationMethod;
import model.Complex;
import model.Matrix;
import model.Vector;

import java.util.List;

public class SVD {

    private SVD(){}

    public static Triple<Matrix, Matrix, Matrix> decompose(Matrix A) {
        List<Pair<Complex, Vector>> eigensAtA = TransformationMethod.findAllEigens(A.T().dot(A));
        List<Pair<Complex, Vector>> eigensAAt = TransformationMethod.findAllEigens(A.dot(A.T()));
        Matrix V = new Matrix();
        Matrix lambda = new Matrix();
        Matrix U = new Matrix();
        eigensAtA.forEach(pair -> {
            int n = lambda.size();
            lambda.set(n, n, MyMath.sqrt(pair._1().getReal()));
            V.addColumn(pair._2());
        });
        eigensAAt.forEach(pair -> U.addColumn(pair._2()));

        return new Triple<>(U, lambda, V.T());
    }

}
