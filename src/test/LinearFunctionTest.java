package test;

import static org.junit.jupiter.api.Assertions.*;

import model.PivotingMode;
import org.junit.jupiter.api.BeforeAll;

import model.Matrix;
import model.Vector;

class LinearFunctionTest {

    private static Matrix A;
    private static Vector b;

    @BeforeAll
    static void setUpBeforeClass() {
        A = new Matrix();
        A.addRow(0.02, 0.01, 0., 0.);
        A.addRow(1., 2., 1., 0.);
        A.addRow(0., 1., 2., 1.);
        A.addRow(0., 0., 100., 200.);

        b = new Vector(0.02, 1., 4., 800.);
    }

    @org.junit.jupiter.api.Test
    void test() {
        assertEquals(new Vector(1., 0., 0., 4.), A.solveByGaussianElimination(b, PivotingMode.NONE), "Gaussian no pivoting");
        assertEquals(new Vector(1., 0., 0., 4.), A.solveByGaussianElimination(b, PivotingMode.PARTIAL), "Gaussian partial pivoting");
        assertEquals(new Vector(1., 0., 0., 4.), A.solveByGaussianElimination(b, PivotingMode.TOTAL), "Gaussian total pivoting");

        assertEquals(5, A.determinant(), 0.0001, "Determinant of A");
    }

}
