package test;

import static org.junit.jupiter.api.Assertions.*;

import model.Complex;
import model.PivotingMode;
import org.junit.jupiter.api.BeforeAll;

import model.Matrix;
import model.Vector;
import solvers.*;

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

    @org.junit.jupiter.api.Test
    void testJacobiSolver() {
        IterativeSolver solver = new JacobiSolver();
        assertEquals(new Vector(1., 0., 0., 4.), solver.solve(A, b), "Jacobi");
        System.out.println(solver.getLastIterations());
    }

    @org.junit.jupiter.api.Test
    void testGaussSeidelSolver() {
        IterativeSolver solver = new GaussSeidelSolver();
        assertEquals(new Vector(1., 0., 0., 4.), solver.solve(A, b), "Gauss-Seidel");
        System.out.println(solver.getLastIterations());
    }

    @org.junit.jupiter.api.Test
    void testSORSolver() {
        IterativeSolver solver = new SORSolver(1.3);
        assertEquals(new Vector(1., 0., 0., 4.), solver.solve(A, b), "SOR");
        System.out.println(solver.getLastIterations());
    }

    @org.junit.jupiter.api.Test
    void testSteepestDescentSolver() {
//        Matrix A = Matrix.parse("[[4, 1], [1, 3]]");
//        Vector b = new Vector(1, 2);
        IterativeSolver solver = new SteepestDescentSolver();
        solver.solve(A, b).show();
//        assertEquals(new Vector(1 / 11., 7 / 11.), solver.solve(A, b), "Simple Conjugate Gradients");
        System.out.println(solver.getLastIterations());
    }

    @org.junit.jupiter.api.Test
    void testSimpleConjugateGradientSolver() {
//        Matrix A = Matrix.parse("[[4, 1], [1, 3]]");
//        Vector b = new Vector(1, 2);
        IterativeSolver solver = new ConjugateGradientSolver();
        solver.solve(A, b).show();
//        assertEquals(new Vector(1 / 11., 7 / 11.), solver.solve(A, b), "Simple Conjugate Gradients");
        System.out.println(solver.getLastIterations());
    }

}
