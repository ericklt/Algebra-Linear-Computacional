package decomposition;

import Utils.Pair;
import model.Matrix;

public interface Decomposition {

    Pair<Matrix, Matrix> decompose(Matrix m);

}
