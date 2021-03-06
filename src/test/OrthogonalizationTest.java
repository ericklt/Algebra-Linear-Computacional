package test;

import Utils.Pair;
import decomposition.GrandShmidtDecomposition;
import decomposition.HouseHolderDecomposition;
import decomposition.JacobiDecomposition;
import model.Matrix;
import model.Vector;
import org.junit.jupiter.api.BeforeAll;
import solvers.Solver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrthogonalizationTest {

    private static Matrix A;
    private static Vector b;

    @BeforeAll
    static void setUpBeforeClass() {
        A = Matrix.parse("[[-1, -1, 1], [1, 3, 3], [-1, -1, 5], [1, 3, 7]]");
        b = new Vector(4., 2., 1., -3.);
    }

    @org.junit.jupiter.api.Test
    void grandSchmidtTest() {
        Pair<Matrix, Matrix> QR = new GrandShmidtDecomposition().decompose(A);
        Matrix Q = QR._1();
        Matrix R = QR._2();
        Q.show();
        R.show();
        Solver.solveByRetroSubstitution(R, Q.T().dot(b)).show();
    }

    @org.junit.jupiter.api.Test
    void houseHolderTest() {
        Pair<Matrix, Matrix> QR = new HouseHolderDecomposition().decompose(A);
        Matrix Q = QR._1();
        Matrix R = QR._2();
        Q.show();
        R.show();
//        R.solveByRetroSubstitution(Q.T().dot(b)).show();
    }

    @org.junit.jupiter.api.Test
    void jacobTest() {
        Pair<Matrix, Matrix> QR = new JacobiDecomposition().decompose(A);
        Matrix Q = QR._1();
        Matrix R = QR._2();
        Q.show();
        R.show();
        Solver.solveByRetroSubstitution(R, Q.T().dot(b)).show();
    }

    @org.junit.jupiter.api.Test
    void jacobPeresTest() {
        Matrix A = Matrix.parse("[[1, 1, -1], [1, -2, 5], [4, 1, 4]]");
        Vector b = new Vector(0, 21, 31);

        Pair<Matrix, Matrix> QR = new JacobiDecomposition().decompose(A);
        Matrix Q = QR._1();
        Matrix R = QR._2();
        Q.show();
        R.show();
        Solver.solveByRetroSubstitution(R, Q.T().dot(b)).show();
    }

}
