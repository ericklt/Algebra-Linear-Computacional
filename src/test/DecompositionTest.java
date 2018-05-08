package test;
import static org.junit.jupiter.api.Assertions.*;

import Utils.Pair;
import model.PivotingMode;
import org.junit.jupiter.api.BeforeAll;

import model.Matrix;
import model.Vector;
import solvers.CholeskySolver;
import solvers.GaussianEliminationSolver;
import solvers.LUSolver;
import solvers.Solver;

class DecompositionTest {

    private static Matrix A;
    private static Vector b;
    private static Solver gaussianSolver;
    private static Solver choleskySolver;
    private static Solver luSolver;

    @BeforeAll
    static void setUpBeforeClass() {
        gaussianSolver = new GaussianEliminationSolver(PivotingMode.NONE);
        choleskySolver = new CholeskySolver();
        luSolver = new LUSolver();
        A = new Matrix();
        A.addRow(20., 10., -4., -10.);
        A.addRow(10., 50., 1., -8.);
        A.addRow(-4., 1., 100., 5.);
        A.addRow(-10., -8., 5., 200.);

        b = new Vector(20., 1., 4., 800.);
    }

    @org.junit.jupiter.api.Test
    void choleskyTest() {
        assertEquals(gaussianSolver.solve(A, b), choleskySolver.solve(A, b), "Solving by Cholesky");
    }

    @org.junit.jupiter.api.Test
    void LUTest() {
        assertEquals(gaussianSolver.solve(A, b), luSolver.solve(A, b), "Solving by LU");
    }

}
