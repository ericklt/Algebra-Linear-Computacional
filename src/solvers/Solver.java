package solvers;

import model.Matrix;
import model.Vector;

public abstract class Solver {

    public abstract Vector solve(Matrix A, Vector b);

    public static Vector solveBySubstitution(Matrix m, Vector b, Matrix colPermutation) {
        return colPermutation.dot(solveBySubstitution(m, b));
    }

    public static Vector solveBySubstitution(Matrix m, Vector b) {
        Vector result = new Vector(b.size());
        for (int i = 0; i < b.size(); i++)
            result.set(i, (b.get(i).sub(m.get(i).dot(result))).div( m.get(i, i) ));
        return result;
    }

    public static Vector solveByRetroSubstitution(Matrix m, Vector b, Matrix colPermutation) {
        return colPermutation.dot(solveByRetroSubstitution(m, b));
    }

    public static Vector solveByRetroSubstitution(Matrix m, Vector b) {
        Vector result = new Vector(b.size());
        for (int i = b.size()-1; i >=0; i--)
            result.set(i, (b.get(i).sub( m.get(i).dot(result) )).div( m.get(i, i) ));
        return result;
    }

}
