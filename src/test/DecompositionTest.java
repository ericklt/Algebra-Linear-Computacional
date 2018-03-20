package test;
import static org.junit.jupiter.api.Assertions.*;

import Utils.Pair;
import model.PivotingMode;
import org.junit.jupiter.api.BeforeAll;

import model.Matrix;
import model.Vector;

class DecompositionTest {

    private static Matrix A;
    private static Vector b;

    @BeforeAll
    static void setUpBeforeClass() {
        A = new Matrix();
        A.addRow(20., 10., -4., -10.);
        A.addRow(10., 50., 1., -8.);
        A.addRow(-4., 1., 100., 5.);
        A.addRow(-10., -8., 5., 200.);

        b = new Vector(20., 1., 4., 800.);
    }

    @org.junit.jupiter.api.Test
    void choleskyTest() {
        assertEquals(A.solveByGaussianElimination(b, PivotingMode.NONE), A.solveByCholesky(b), "Solving by Cholesky");
    }

    @org.junit.jupiter.api.Test
    void LUTest() {
        assertEquals(A.solveByGaussianElimination(b, PivotingMode.NONE), A.solveByLU(b), "Solving by LU");
    }

}
