package decomposition;

import Utils.Pair;
import decomposition.Decomposition;
import model.Matrix;
import model.Vector;

public class GrandShmidtDecomposition implements Decomposition {
    @Override
    public Pair<Matrix, Matrix> decompose(Matrix m) {
        Matrix ortho = new Matrix();
        for (int c = 0; c < m.shape()[1]; c++) {
            Vector col = m.getColumn(c);
            for (int i = 0; i < c; i++)
                col = col.subtract(ortho.get(i).dot(ortho.get(i).dot(col)));
            ortho.addRow(col.normalized());
        }
        return new Pair<>(ortho.T(), ortho.dot(m));
    }
}
