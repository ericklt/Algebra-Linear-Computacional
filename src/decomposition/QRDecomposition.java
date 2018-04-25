package decomposition;

import Utils.Pair;
import model.Matrix;

public interface QRDecomposition {

    Pair<Matrix, Matrix> decompose(Matrix m);

}
