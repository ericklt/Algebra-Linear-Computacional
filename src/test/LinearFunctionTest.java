package test;

import static org.junit.jupiter.api.Assertions.*;

import model.Complex;
import model.PivotingMode;
import org.junit.jupiter.api.BeforeAll;

import model.Matrix;
import model.Vector;
import solvers.GaussianEliminationSolver;
import solvers.LeastSquaresSolver;
import solvers.Solver;

class LinearFunctionTest {

    private static Matrix A;
    private static Vector b;
    private static GaussianEliminationSolver gaussianSolver;
    private static Solver leastSquaresSolver;

    @BeforeAll
    static void setUpBeforeClass() {
        gaussianSolver = new GaussianEliminationSolver();
        leastSquaresSolver = new LeastSquaresSolver();
        A = new Matrix();
        A.addRow(0.02, 0.01, 0., 0.);
        A.addRow(1., 2., 1., 0.);
        A.addRow(0., 1., 2., 1.);
        A.addRow(0., 0., 100., 200.);

        b = new Vector(0.02, 1., 4., 800.);
    }

    @org.junit.jupiter.api.Test
    void test() {
        gaussianSolver.setMode(PivotingMode.NONE);
        assertEquals(new Vector(1., 0., 0., 4.), gaussianSolver.solve(A, b), "Gaussian no pivoting");
        gaussianSolver.setMode(PivotingMode.PARTIAL);
        assertEquals(new Vector(1., 0., 0., 4.), gaussianSolver.solve(A, b), "Gaussian partial pivoting");
        gaussianSolver.setMode(PivotingMode.TOTAL);
        assertEquals(new Vector(1., 0., 0., 4.), gaussianSolver.solve(A, b), "Gaussian total pivoting");

        assertEquals(new Complex(5), A.determinant(), "Determinant of A");
    }

    @org.junit.jupiter.api.Test
    void testLeastSquares() {
        assertEquals(new Vector(1., 0., 0., 4.), leastSquaresSolver.solve(A, b), "Least squares");
    }

}
